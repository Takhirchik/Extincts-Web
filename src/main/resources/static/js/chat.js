document.addEventListener('DOMContentLoaded', function() {
    // Проверяем наличие всех необходимых элементов в DOM
    const requiredElements = [
        'contacts', 'recipient-name', 'recipient-avatar',
        'recipient-info', 'no-recipient', 'messages',
        'message-form', 'message-content', 'send-button'
    ];

    for (const id of requiredElements) {
        if (!document.getElementById(id)) {
            console.error(`Element with id '${id}' not found in DOM`);
            return;
        }
    }

    const token = localStorage.getItem('token');
    if (!token) {
        window.location.href = '/login';
        return;
    }

    let stompClient = null;
    let currentRecipient = null;
    let currentUser = null;

    function updateCurrentUserUI(user) {
        const currentUserElement = document.querySelector('.current-user');
        if (currentUserElement) {
            const avatar = currentUserElement.querySelector('.user-avatar');
            const nameElement = currentUserElement.querySelector('span');

            if (avatar && user.smallThumbnailUrl) {
                avatar.src = user.smallThumbnailUrl;
            }
            if (nameElement) {
                nameElement.textContent = user.nickname || user.login;
            }
        }
    }

    function connect() {
        const socket = new SockJS('/websocket');
        stompClient = Stomp.over(socket);

        stompClient.connect({
            'X-Authorization': `Bearer ${token}`
        }, function(frame) {
            console.log('Connected: ' + frame);

            fetchCurrentUser().then(user => {
                currentUser = user;
                updateCurrentUserUI(user);
                loadContacts();
            });
        }, function(error) {
            console.error('WebSocket connection error:', error);
            showError('Connection error. Please refresh the page.');
        });
    }

    function fetchCurrentUser() {
        return fetch('/user/current', {
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        })
        .then(response => {
            if (!response.ok) throw new Error('Failed to fetch current user');
            return response.json();
        })
        .catch(error => {
            console.error('Error fetching current user:', error);
            showError('Failed to load user data');
            throw error;
        });
    }

    function loadContacts() {
        console.log('Loading contacts...');

        fetch('/user/all', {
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        })
        .then(response => {
            if (!response.ok) throw new Error('Failed to load contacts');
            return response.json();
        })
        .then(users => {
            console.log('Contacts loaded:', users);
            const contactsList = document.getElementById('contacts');
            if (!contactsList) {
                console.error('Contacts list element not found');
                return;
            }

            contactsList.innerHTML = '';

            if (!users || users.length === 0) {
                contactsList.innerHTML = '<li class="no-contacts">No contacts available</li>';
                return;
            }

            users.forEach(user => {
                const li = document.createElement('li');
                li.className = 'contact';
                li.dataset.userId = user.id;
                li.innerHTML = `
                    <div class="contact-info">
                        <img src="${user.smallThumbnailUrl || '/images/default-avatar.png'}"
                             alt="${user.nickname}" class="contact-avatar">
                        <div class="contact-details">
                            <span class="contact-name">${user.nickname || user.login}</span>
                        </div>
                    </div>
                `;
                li.addEventListener('click', () => loadConversation(user));
                contactsList.appendChild(li);
            });

            if (users.length > 0) {
                loadConversation(users[0]);
            }
        })
        .catch(error => {
            console.error('Error loading contacts:', error);
            const contactsList = document.getElementById('contacts');
            if (contactsList) {
                contactsList.innerHTML = '<li class="error">Error loading contacts</li>';
            }
            showError('Failed to load contacts');
        });
    }

    function loadConversation(user) {
        if (!user) return;

        currentRecipient = user;
        const recipientName = document.getElementById('recipient-name');
        const recipientAvatar = document.getElementById('recipient-avatar');
        const recipientInfo = document.getElementById('recipient-info');
        const noRecipient = document.getElementById('no-recipient');
        const messageForm = document.getElementById('message-form');

        if (recipientName) recipientName.textContent = user.nickname || user.login;
        if (recipientAvatar) recipientAvatar.src = user.smallThumbnailUrl || '/images/default-avatar.png';
        if (recipientInfo) recipientInfo.style.display = 'flex';
        if (noRecipient) noRecipient.style.display = 'none';
        if (messageForm) messageForm.style.display = 'flex';

        fetch(`/messages/history/${user.id}`, {
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        })
        .then(response => {
            if (!response.ok) throw new Error('Failed to load messages');
            return response.json();
        })
        .then(messages => {
            const messagesContainer = document.getElementById('messages');
            if (!messagesContainer) return;

            messagesContainer.innerHTML = '';

            if (messages.length === 0) {
                messagesContainer.innerHTML = '<div class="no-messages">No messages yet</div>';
                return;
            }

            messages.forEach(msg => {
                displayMessage(msg, msg.senderId === currentUser.id ? 'sent' : 'received');
            });

            messagesContainer.scrollTop = messagesContainer.scrollHeight;
        })
        .catch(error => {
            console.error('Error loading conversation:', error);
            showError('Failed to load messages');
        });
    }

    function displayMessage(message, type) {
        const messagesContainer = document.getElementById('messages');
        if (!messagesContainer) return;

        const messageElement = document.createElement('div');
        messageElement.className = `message ${type}`;

        const timestamp = new Date(message.timestamp);
        const timeString = timestamp.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });

        messageElement.innerHTML = `
            <div class="message-sender">
                <img src="${type === 'sent' ?
                    (currentUser.smallThumbnailUrl || '/images/default-avatar.png') :
                    (currentRecipient.smallThumbnailUrl || '/images/default-avatar.png')}"
                     alt="Avatar" class="message-avatar">
            </div>
            <div class="message-content-wrapper">
                <div class="message-content">${message.content}</div>
                <div class="message-time">${timeString}</div>
            </div>
        `;
        messagesContainer.appendChild(messageElement);
        messagesContainer.scrollTop = messagesContainer.scrollHeight;
    }

    function showError(message) {
        console.error('Error:', message);
        // Можно добавить отображение ошибки в UI
        const errorElement = document.createElement('div');
        errorElement.className = 'global-error';
        errorElement.textContent = message;
        document.body.appendChild(errorElement);
        setTimeout(() => errorElement.remove(), 5000);
    }

    function sendMessage() {
        const messageInput = document.getElementById('message-content');
        if (!messageInput) return;

        const content = messageInput.value.trim();
        if (content && currentRecipient && stompClient) {
            const message = {
                recipientId: currentRecipient.id,
                content: content
            };

            stompClient.send("/app/process-message", {}, JSON.stringify(message));

            displayMessage({
                content: content,
                timestamp: new Date().toISOString(),
                senderId: currentUser.id,
                senderName: currentUser.nickname || currentUser.login,
                senderSmallThumbnailUrl: currentUser.smallThumbnailUrl,
                recipientId: currentRecipient.id
            }, 'sent');

            messageInput.value = '';
        }
    }

    // Initialize
    connect();

    // Event listeners
    const sendButton = document.getElementById('send-button');
    if (sendButton) {
        sendButton.addEventListener('click', sendMessage);
    }

    const messageInput = document.getElementById('message-content');
    if (messageInput) {
        messageInput.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                sendMessage();
            }
        });
    }

    window.addEventListener('beforeunload', function() {
        if (stompClient) {
            stompClient.disconnect();
        }
    });
});