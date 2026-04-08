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
        setMessage('Registration successful! You can now log in.');
        setUsername('');
        setEmail('');
        setPassword('');
      } else {
        setMessage('Error during registration. Please check your details.');
      }
    } catch (error) {
      console.error('Error connecting:', error);
      setMessage('Error connecting to the server.');
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
      {message && <p>{message}</p>}
    </div>
  );
}