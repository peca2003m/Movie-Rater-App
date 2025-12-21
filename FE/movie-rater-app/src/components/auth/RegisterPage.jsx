import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../../api/movieApi';

function RegisterPage({ onLogin }) {
    const [isLogin, setIsLogin] = useState(false);

    const navigate = useNavigate();

    const [loginData, setLoginData] = useState({
        firstName: '',
        lastName: '',
        username: '',
        password: ''
    });

    const [error, setError] = useState('');

    const handleSubmit = async () => {
        setError('');

        try {
                const response = await api.register(loginData);
                if (response.status === 400) {
                    setError('Registration failed. Username may already exist.'); //todo fix this
                    return;
                }
                alert('Registration successful! Please login.');
                
                navigate('/');

        } catch (err) {
            setError(err.message === 'Unauthorized' ? 'Invalid credentials' : 'An error occurred');
            sessionStorage.removeItem('auth');
        }
    };

    return (
        <div className="min-h-screen bg-gradient-to-br from-purple-900 via-blue-900 to-indigo-900 flex items-center justify-center p-4">
            <div className="bg-white rounded-lg shadow-2xl p-8 w-full max-w-md">
                <h1 className="text-3xl font-bold text-center mb-6 text-gray-800">
                    Movie Collection App
                </h1>    

                <div className="space-y-4">
                    {!isLogin && (
                        <input
                            type="text"
                            placeholder="First Name"
                            value={loginData.firstName}
                            onChange={(e) => setLoginData({ ...loginData, firstName: e.target.value })}
                            className="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500 outline-none"
                        /> 
                    )}
                    {!isLogin && (
                        <input
                            type="text"
                            placeholder="Last Name"
                            value={loginData.lastName}
                            onChange={(e) => setLoginData({ ...loginData, lastName: e.target.value })}
                            className="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500 outline-none"
                        />
                    )}
                    <input
                        type="text"
                        placeholder="Username"
                        value={loginData.username}
                        onChange={(e) => setLoginData({ ...loginData, username: e.target.value })}
                        className="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500 outline-none"
                    />
                    <input
                        type="password"
                        placeholder="Password"
                        value={loginData.password}
                        onChange={(e) => setLoginData({ ...loginData, password: e.target.value })}
                        onKeyPress={(e) => e.key === 'Enter' && handleSubmit()}
                        className="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500 outline-none"
                    />
                    {error && <p className="text-red-500 text-sm">{error}</p>}
                    <button
                        onClick={handleSubmit}
                        className="w-full bg-blue-600 text-white py-2 rounded-lg hover:bg-blue-700 transition"
                    >
                        {isLogin ? 'Login' : 'Register'}
                    </button>
                </div>
            </div>
        </div>
    );
}

export default RegisterPage;