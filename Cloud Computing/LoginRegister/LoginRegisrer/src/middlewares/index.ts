import express from 'express';
import { get, merge } from 'lodash';

import { getUsersByToken } from '../db/users';

//Check Autentikasi
export const isAuthenticated = async (req: express.Request, res: express.Response, next: express.NextFunction) => {
    try {
        const token = req.cookies['LOGIN-AUTH']; // Mengubah "sessionToken" menjadi "token"

        if (!token) { // Mengubah "sessionToken" menjadi "token"
            return res.sendStatus(403);
        }

        const existingUser = await getUsersByToken(token); // Mengubah "getUsersBySessionToken" menjadi "getUsersByToken"

        if (!existingUser)  {
            return res.sendStatus(403);
        }

        merge(req, { identity: existingUser });

        return next();
    } catch (error) {
        console.log(error);
        return res.sendStatus(400);
    }
}

//Ini kode untuk memvalidasi akun sendiri fungsinya agar tidak bisa menghapus akun lain kalo login pake akun sendiri
export const isOwner = async (req: express.Request, res: express.Response, next: express.NextFunction) => {
    try {
        const {id} = req.params
        const currenUserId = get(req, 'identity._id') as string

        if (!currenUserId) {
            return res.sendStatus(403);
        }

        if (currenUserId.toString() !== id) {
            return res.sendStatus(403);
        }

        next();
    } catch (error) {
        console.log(error);
        return res.sendStatus(400)
    }
}
