import { useState } from 'react';

export default function Register() {
  // Stany odpowiadające polom z RegisterUserDto
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [message, setMessage] = useState('');

  const handleRegister = async (e) => {
    e.preventDefault(); // Zatrzymuje przeładowanie strony po wysłaniu formularza

    try {
      const response = await fetch('http://localhost:8080/auth/signup', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        // Tworzymy JSON zgodny z Twoim RegisterUserDto
        body: JSON.stringify({ username, email, password }),
      });

      if (response.ok) {
        setMessage('Rejestracja przebiegła pomyślnie! Możesz się zalogować.');
        setUsername('');
        setEmail('');
        setPassword('');
      } else {
        setMessage('Błąd podczas rejestracji. Sprawdź poprawność danych.');
      }
    } catch (error) {
      console.error('Błąd połączenia:', error);
      setMessage('Błąd połączenia z serwerem.');
    }
  };

  return (
    <div style={{ maxWidth: '300px', margin: 'auto', padding: '20px' }}>
      <h2>Rejestracja</h2>
      <form onSubmit={handleRegister} style={{ display: 'flex', flexDirection: 'column', gap: '10px' }}>
        <input
          type="text"
          placeholder="Nazwa użytkownika"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          required
        />
        <input
          type="email"
          placeholder="Adres e-mail"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
        />
        <input
          type="password"
          placeholder="Hasło"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
        />
        <button type="submit">Zarejestruj się</button>
      </form>
      {message && <p>{message}</p>}
    </div>
  );
}