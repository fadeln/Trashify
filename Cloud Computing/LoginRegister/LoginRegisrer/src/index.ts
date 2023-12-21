import express from 'express';
import http from 'http';
import bodyParser from 'body-parser';
import cookieParser from 'cookie-parser';
import compression from 'compression';
import cors from 'cors';
import mongoose from 'mongoose';

import router from './router';

const app = express();

app.use(cors({
    credentials: true,
}));

app.use(compression());
app.use(cookieParser());
//accepts body
app.use(express.json());
app.use(express.urlencoded({ extended: false }));

//Informasi server sedang berjalan
const server = http.createServer(app);
server.listen(process.env.PORT || 8080, () => {
    console.log(`Server berhasil berjalan di ${process.env.APP_URL || 'http://localhost'}:${process.env.PORT || 8080}/`);
});

//Base URI MONGO
const MONGO_URL = 'mongodb+srv://netrava:Indonesia45@cluster-1.ntofxpo.mongodb.net/?retryWrites=true&w=majority';

mongoose.Promise = Promise;
mongoose.connect(MONGO_URL);
mongoose.connection.on('error', (error: Error) => console.log(error))

//Tempat router berjalan
app.use('/', router());