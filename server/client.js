const { io } = require('socket.io-client');

const socket = io('ws://localhost:3333');

socket.on('notificationsUnread', notification => {
  console.log(notification);
});
