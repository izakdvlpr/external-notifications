const http = require('http');
const { randomUUID } = require('node:crypto');

const express = require('express');
const cors = require('cors');

const { Server: WebsocketServer } = require('socket.io');

const app = express();
const server = http.createServer(app);
const ws = new WebsocketServer(server, {
  cors: { origin: '*' },
});

app.use(express.json());
app.use(express.urlencoded({ extended: false }));

app.use(cors());

const fakeRecipientId = '84b4192a-e8cb-4dbc-a370-8807eed43e97';
const fakeNotificationsData = [];

app.post('/notifications', (request, response) => {
  const { content } = request.body;

  const newNotification = {
    id: randomUUID(),
    recipientId: fakeRecipientId,
    content,
    readAt: null,
  };

  fakeNotificationsData.push(newNotification);

  ws.emit('notificationCreate', newNotification);

  ws.emit(
    'notificationsUnread',
    fakeNotificationsData.filter(({ readAt }) => !readAt),
  );

  response.status(200).send();
});

app.get('/notifications', (request, response) => {
  response.json(fakeNotificationsData);
});

app.patch('/notifications/:id/read', (request, response) => {
  const { id } = request.params;

  const notification = fakeNotificationsData.find(nt => nt.id === id);

  if (!notification) {
    response.status(400).json({ message: 'Notification not found.' });

    return;
  }

  notification.readAt = new Date();

  response.status(200).send();
});

server.listen(3333, () => console.log('Api running.'));
