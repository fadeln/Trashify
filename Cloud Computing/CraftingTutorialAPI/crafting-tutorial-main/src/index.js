const express = require('express');
const http = require('http');
const bodyParser = require('body-parser');
const cookieParser = require('cookie-parser');
const compression = require('compression');
const cors = require('cors');
const mongoose = require('mongoose');

const router = require('./router');

const app = express();

const server = http.createServer(app);



const MONGO_URL = 'mongodb+srv://capstone-crafting:12345@cluster0.d0knz91.mongodb.net/?retryWrites=true&w=majority';

mongoose.Promise = global.Promise;
mongoose.connect(MONGO_URL);
mongoose.connection.on('error', (error) => console.log(error));

app.use('/', router());

server.listen(8080, () => {
    console.log('Server is running on http://localhost:8080/tutorial/');

});