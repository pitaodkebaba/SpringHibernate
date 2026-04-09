import { useState } from 'react';

export default function Login({ onLoginSuccess }) {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [message, setMessage] = useState('');
  const [isError, setIsError] = useState(false); 

  const handleLogin = async (e) => {
    e.preventDefault();
    setMessage('Logowanie...'); 
    setIsError(false);

    try {
      const response = await fetch('/auth/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ username, password }),
      });

      if (response.ok) {
        const data = await response.json();
        localStorage.setItem('token', data.token);
        setMessage('Login successful!');
        setIsError(false);
        if (onLoginSuccess) onLoginSuccess();
      } else {
        setIsError(true);
        try {
          const errorData = await response.json();
          if (errorData && errorData.error) {
            setMessage(errorData.error); 
          } else {
            setMessage('Błędna nazwa użytkownika lub hasło.');
          }
        } catch (parseError) {
          setMessage(`Błąd serwera (Status: ${response.status})`);
        }
      }
    } catch (error) {
      console.error('Błąd połączenia fetch:', error);
      setIsError(true);
      setMessage('Brak połączenia z serwerem Gateway.');
    }
  };

  return (
    <div style={{ maxWidth: '300px', margin: 'auto', padding: '20px' }}>
      <h2>Login</h2>
      <form onSubmit={handleLogin} style={{ display: 'flex', flexDirection: 'column', gap: '10px' }}>
        <input
          type="text"
          placeholder="Username"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          required
        />
        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
        />
        <button type="submit">Login</button>
      </form>
      {/* Wyświetlanie wiadomości (czerwona dla błędów, zielona/czarna dla sukcesu) */}
      {message && (
        <p style={{ color: isError ? '#e22134' : 'inherit', marginTop: '15px', fontWeight: 'bold' }}>
          {message}
        </p>
      )}
    </div>
  );
}