import { createUser, getUsersByEmail } from '../db/users';
import express from 'express';
import { authentication, random } from '../helpers';

// Fungsi untuk melakukan login
export const login = async (req: express.Request, res: express.Response) => {
    try {
        const { email, password } = req.body;

        if (!email || !password) {
            return res.status(400).json({ error: true, message: 'failed' });
        }

        const user = await getUsersByEmail(email).select('+authentication.salt +authentication.password')

        if (!user) {
            return res.status(400).json({ error: true, message: 'failed' });
        }

        const expectedHash = authentication(user.authentication.salt, password);

        if (user.authentication.password !== expectedHash) {
            return res.status(403).json({ error: true, message: 'failed' });
        }

        const salt = random();
        user.authentication.token = authentication(salt, user._id.toString());

        await user.save();

        res.cookie('LOGIN-AUTH', user.authentication.token, { domain : 'localhost', path: '/' }); 

        return res.status(200).json({
            error: false,
            message: 'success',
            loginResult: {
                userId: user._id.toString(),
                name: user.name,
                token: user.authentication.token
            }
        });
    } catch (error) {
        console.log(error);
        return res.status(400).json({ error: true, message: 'failed' });
    }
};
// Fungsi untuk melakukan registrasi
export const register = async (req: express.Request, res: express.Response) => {
    try {
        const { email, password, name } = req.body; 

        if (!email || !password || !name) { 
            return res.status(400).json({ error: true, message: 'failed' });
        }

        const existingUser = await getUsersByEmail(email);

        if (existingUser) {
            return res.status(400).json({ error: true, message: 'failed' });
        }

        const salt = random()
        const user = await createUser({
            email,
            name,
            authentication: {
                salt,
                password: authentication(salt, password)
            }
        })

        return res.status(200).json({
            error: false,
            message: 'success',
            registerResult: {
                userId: user._id.toString(),
                name: user.name,
                token: user.authentication.token
            }
        });
    } catch (error) {
        console.log(error);
        return res.status(400).json({ error: true, message: 'failed' });
    }
}