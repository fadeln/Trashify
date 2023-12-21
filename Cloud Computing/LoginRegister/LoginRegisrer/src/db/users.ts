import mongoose from "mongoose";

const userSchema = new mongoose.Schema({
    name: { // Mengubah "username" menjadi "name"
        type : String,
        required : true
    },
    email: {
        type : String,
        required: true
    },
    authentication: {
        password: { 
            type: String,
            required: true,
            select: false
        },
        salt: {
            type: String,
            select: false
        },
        token : { // Mengubah "sessionToken" menjadi "token"
            type: String,
            select: false
        },
    }
});

export const userModel = mongoose.model('User', userSchema);

// Fungsi untuk mendapatkan semua pengguna dari database
export const getUsers = () => userModel.find();

// Fungsi untuk mendapatkan pengguna berdasarkan email dari database
export const getUsersByEmail = (email: string) => userModel.findOne({ email });

// Fungsi untuk mendapatkan pengguna berdasarkan token dari database
export const getUsersByToken = (token: string) => userModel.findOne({ // Mengubah "getUsersBySessionToken" menjadi "getUsersByToken"
    'authentication.token': token, // Mengubah "sessionToken" menjadi "token"
});

// Fungsi untuk mendapatkan pengguna berdasarkan ID dari database
export const getUserById = (id: string) => userModel.findById(id);

// Fungsi untuk membuat pengguna baru di database
export const createUser = (values: Record<string, any>) => new userModel(values).save().then((user) => user.toObject());

// Fungsi untuk menghapus pengguna berdasarkan ID dari database
export const deleteUserById = (id: string) => userModel.findOneAndDelete({ _id: id});

// Fungsi untuk memperbarui pengguna berdasarkan ID di database
export const updateUserById = (id: string, values: Record<string, any>) => userModel.findByIdAndUpdate(id, values);

