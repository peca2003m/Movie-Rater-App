import React, { useState } from 'react';
import api from '../../api/movieApi';

function AuthPage({ onLogin }) {
    const [isLogin, setIsLogin] = useState(true);

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
            if (isLogin) {
                const auth = 'Basic ' + btoa(`${loginData.username}:${loginData.password}`);
                sessionStorage.setItem('auth', auth);
                const user = await api.getMe();
                onLogin(user);
            } else {
                if (!loginData.email) {
                    setError('Please enter your email address.');
                    return;
                }
                
                try {
                    console.log('Sending email:', loginData.email); 
                    const response = await api.preRegister({ email: loginData.email });
                    console.log('Response:', response); 
                    alert('Registration email sent! Please check your inbox.');
                } catch (err) {
                    console.error('Full error:', err); 
                    setError('Failed to send registration email. Please try again.');
                }
            }
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

                <div className="flex mb-6 bg-gray-100 rounded-lg p-1">
                    <button
                        onClick={() => setIsLogin(true)}
                        className={`flex-1 py-2 rounded-md transition ${isLogin ? 'bg-white shadow text-blue-600' : 'text-gray-600'}`}
                    >
                        Login
                    </button>
                    <button
                        onClick={() => setIsLogin(false)}
                        className={`flex-1 py-2 rounded-md transition ${!isLogin ? 'bg-white shadow text-blue-600' : 'text-gray-600'}`}
                    >
                        Register
                    </button>
                </div>

                <div className="space-y-4">
                    {!isLogin ? (
                        <input
                            type="email"
                            placeholder="Email"
                            value={loginData.email}
                            onChange={(e) => setLoginData({ ...loginData, email: e.target.value })}
                            className="w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-blue-500 outline-none"
                        />
                    ) : (
                        <>
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
                        </>
                    )}
                    {error && <p className="text-red-500 text-sm">{error}</p>}
                    <button
                        onClick={handleSubmit}
                        className="w-full bg-blue-600 text-white py-2 rounded-lg hover:bg-blue-700 transition"
                    >
                        {isLogin ? 'Login' : 'Send registraion email'}
                    </button>
                </div>
            </div>
        </div>
    );
}

export default AuthPage;