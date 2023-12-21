import express from 'express';
import { login, register, } from '../controllers/authentication';

// Router untuk login, register, dan logout
export default (router: express.Router) => {
    router.post('/auth/register', register);
    router.post('/auth/login', login);
}
