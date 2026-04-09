import { useState } from 'react';

export default function Register() {
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [message, setMessage] = useState('');
  const [isError, setIsError] = useState(false); 

  const handleRegister = async (e) => {
    e.preventDefault();
    setMessage('Rejestracja...'); 
    setIsError(false);

    try {
      const response = await fetch('/auth/signup', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ username, email, password }),
      });

      if (response.ok) {
        setMessage('Registration successful! You can now log in.');
        setIsError(false); 
        setUsername('');
        setEmail('');
        setPassword('');
      } else {
        setIsError(true);
        try {
          const errorData = await response.json();
          if (errorData && errorData.error) {
            setMessage(errorData.error);
          } else {
            setMessage('Błąd podczas rejestracji. Sprawdź poprawność danych.');
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
      <h2>Register</h2>
      <form onSubmit={handleRegister} style={{ display: 'flex', flexDirection: 'column', gap: '10px' }}>
        <input
          type="text"
          placeholder="Username"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          required
        />
        <input
          type="email"
          placeholder="Email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
        />
        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
        />
        <button type="submit">Register</button>
      </form>
      {/* Wyświetlanie wiadomości (czerwona dla błędów, zielona dla sukcesu) */}
      {message && (
        <p style={{ color: isError ? '#e22134' : '#1db954', marginTop: '15px', fontWeight: 'bold', textAlign: 'center' }}>
          {message}
        </p>
      )}
    </div>
  );
}