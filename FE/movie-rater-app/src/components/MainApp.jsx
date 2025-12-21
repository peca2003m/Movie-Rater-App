import React, { useState, useEffect } from 'react';
import api from '../api/movieApi';
import AuthPage from './auth/AuthPage';
import Dashboard from './dashboard/Dashboard';

function MainApp() {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        checkAuth();
    }, []);

    const checkAuth = async () => {
        try {
            const userData = await api.getMe();
            setUser(userData);
        } catch (err) {
            sessionStorage.removeItem('auth');
        }
        setLoading(false);
    };

    const handleLogout = () => {
        sessionStorage.removeItem('auth');
        setUser(null);
    };

    if (loading) {
        return (
            <div className="min-h-screen flex items-center justify-center">
                <div className="text-xl">Loading...</div>
            </div>
        );
    }

    return user ? (
        <Dashboard user={user} onLogout={handleLogout} />
    ) : (
        <AuthPage onLogin={setUser} />
    );
}

export default MainApp;