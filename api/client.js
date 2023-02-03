const { io } = require('socket.io-client');

const socket = io('ws://localhost:3333', {
  query: {
    userId: 'izak',
  },
});

socket.on('notificationsUnread', notification => {
  console.log(notification);
});
