import { useState } from 'react';

export default function Login() {
  // Stany odpowiadające polom z LoginUserDto
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [message, setMessage] = useState('');

  const handleLogin = async (e) => {
    e.preventDefault();

    try {
      const response = await fetch('http://localhost:8080/auth/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        // Tworzymy JSON zgodny z Twoim LoginUserDto
        body: JSON.stringify({ username, password }),
      });

      if (response.ok) {
        const data = await response.json();
        // Zapisujemy token JWT do localStorage, aby używać go w przyszłych zapytaniach
        localStorage.setItem('token', data.token);
        setMessage('Zalogowano pomyślnie!');
      } else {
        setMessage('Błędny login lub hasło.');
      }
    } catch (error) {
      console.error('Błąd połączenia:', error);
      setMessage('Błąd połączenia z serwerem.');
    }
  };

  return (
    <div style={{ maxWidth: '300px', margin: 'auto', padding: '20px' }}>
      <h2>Logowanie</h2>
      <form onSubmit={handleLogin} style={{ display: 'flex', flexDirection: 'column', gap: '10px' }}>
        <input
          type="text"
          placeholder="Nazwa użytkownika"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          required
        />
        <input
          type="password"
          placeholder="Hasło"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
        />
        <button type="submit">Zaloguj się</button>
      </form>
      {message && <p>{message}</p>}
    </div>
  );
}