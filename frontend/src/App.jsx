import Login from './auth/Login.jsx';
import Register from './auth/Register.jsx';

function App() {
  return (
    <div style={{ display: 'flex', justifyContent: 'space-around', marginTop: '50px' }}>
      <Register />
      <Login />
    </div>
  );
}

export default App;