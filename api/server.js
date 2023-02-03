const http = require('http');
const { randomUUID } = require('node:crypto');

const express = require('express');
const cors = require('cors');
const { Server: WebsocketServer } = require('socket.io');

const app = express();
const server = http.createServer(app);
const ws = new WebsocketServer(server, {
  cors: {
    origin: '*',
  },
});

app.use(express.json());
app.use(
  express.urlencoded({
    extended: false,
  }),
);

app.use(cors());

const connectionsData = {};
const notificationsData = [];

ws.on('connection', socket => {
  const { userId } = socket.handshake.query;

  if (!userId) return;

  connectionsData[userId] = socket.id;

  console.log('Client connected:', userId);
});

app.post('/notifications', (request, response) => {
  const { recipientId, content } = request.body;

  const newNotification = {
    id: randomUUID(),
    recipientId,
    content,
    readAt: null,
  };

  notificationsData.push(newNotification);

  const userSocket = connectionsData[recipientId];

  ws.to(userSocket).emit('notificationCreate', newNotification);

  ws.to(userSocket).emit(
    'notificationsUnread',
    notificationsData.filter(({ readAt }) => !readAt),
  );

  response.status(200).send();
});

app.get('/notifications', (request, response) => {
  response.json(notificationsData);
});

app.patch('/notifications/:id/read', (request, response) => {
  const { id } = request.params;

  const notification = notificationsData.find(
    ({ id: notificationId }) => notificationId === id,
  );

  if (!notification) {
    response.status(400).json({ message: 'Notification not found.' });

    return;
  }

  notification.readAt = new Date();

  response.status(200).send();
});

server.listen(3333, () => console.log('Api running.'));
