import express from 'express';
import { deleteUserById, getUserById, getUsers } from '../db/users';

// Fungsi untuk mendapatkan semua pengguna
export const getAllUsers = async (req: express.Request, res: express.Response) => {
    try {
        // Mengambil semua pengguna dari database
        const user = await getUsers();

        // Mengirimkan respon dengan status 200 dan data pengguna
        return res.status(200).json(user);
    } catch (error) {
        // Menangani kesalahan dan mengirimkan respon dengan status 400
        console.log(error);
        return res.sendStatus(400);
    }
};

// Fungsi untuk menghapus pengguna berdasarkan ID
export const deleteUser = async (req: express.Request, res: express.Response) => {
    try {
        // Mengambil ID dari parameter request
        const { id } = req.params;

        // Menghapus pengguna berdasarkan ID
        const deletedUser = await deleteUserById(id);

        // Mengirimkan respon dengan data pengguna yang dihapus
        return res.json(deletedUser);
    } catch (error) {
        // Menangani kesalahan dan mengirimkan respon dengan status 400
        console.log(error);
        return res.sendStatus(400);
    }
};

// Fungsi untuk memperbarui pengguna
export const updateUser = async (req: express.Request, res: express.Response) => {
    try {
        // Mengambil ID dan name dari request
        const { id } = req.params;
        const { name } = req.body; 

        // Memeriksa apakah name ada
        if (!name) { 
            return res.sendStatus(400);
        }

        // Mengambil pengguna berdasarkan ID
        const user = await getUserById(id);

        // Memperbarui name pengguna
        user.name = name; // Mengubah "username" menjadi "name"

        // Menyimpan perubahan ke database
        await user.save();

        // Mengirimkan respon dengan status 200 dan data pengguna
        return res.status(200).json(user).end();
    } catch (error) {
        // Menangani kesalahan dan mengirimkan respon dengan status 400
        console.log(error);
        return res.sendStatus(400);
    }
};

// Fungsi untuk melakukan logout
export const logout = async (req: express.Request, res: express.Response) => {
    try {
        // Mengambil ID pengguna dari token
        const { id } = req.params;

        // Mengambil pengguna berdasarkan ID
        const user = await getUserById(id);

        // Menghapus token autentikasi pengguna
        user.authentication.token = null;

        // Menyimpan perubahan ke database
        await user.save();

        // Menghapus cookie
        res.clearCookie('LOGIN-AUTH', { domain : 'localhost', path: '/' });

        // Mengirimkan respon dengan status 200 dan pesan sukses
        return res.status(200).json({ error: false, message: 'Logout successful' });
    } catch (error) {
        // Menangani kesalahan dan mengirimkan respon dengan status 400
        console.log(error);
        return res.status(400).json({ error: true, message: 'Logout failed' });
    }
};
