import { useState, useEffect } from 'react';
import './SongList.css';

export default function SongList() {
  const [songs, setSongs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchSongs = async () => {
      const token = localStorage.getItem('token');
      if (!token) {
        setError('Token not found. Please log in again.');
        setLoading(false);
        return;
      }
      try {
        const response = await fetch('/api/songs', {
          method: 'GET',
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
          }
        });
        if (response.ok) {
          const data = await response.json();
          // Zabezpieczenie przed błędem e.map is not a function
          if (Array.isArray(data)) {
            setSongs(data);
          } else if (data && Array.isArray(data.data)) {
            setSongs(data.data);
          } else if (data && Array.isArray(data.content)) {
             setSongs(data.content);
          } else {
            console.error("Backend returned unexpected data structure:", data);
            setSongs([]); 
          }
        } else if (response.status === 401 || response.status === 403) {
          setError('No access or session expired. Please log in again.');
        } else {
          setError('An error occurred while fetching songs.');
        }
      } catch (err) {
        console.error(err);
        setError('Error connecting to the server.');
      } finally {
        setLoading(false);
      }
    };
    fetchSongs();
  }, []);

  const handleLogout = () => {
    localStorage.removeItem('token');
    window.location.reload();
  };

  if (loading) return <div className="songlist-container" style={{justifyContent: 'center', alignItems: 'center'}}><h2>Loading library...</h2></div>;
  if (error) return <div className="songlist-container" style={{justifyContent: 'center', alignItems: 'center'}}><h2 style={{color: '#e22134'}}>{error}</h2></div>;

  return (
    <div className="songlist-container">
      {/* NAGŁÓWEK */}
      <div className="songlist-header">
        <h1>MusicBrowser</h1>
        <button className="songlist-logout-btn" onClick={handleLogout}>
          Log out
        </button>
      </div>

      {/* ZAWARTOŚĆ GŁÓWNA */}
      <div className="songlist-content">
        <h2>Available Songs</h2>
        
        {/* Nagłówki kolumn */}
        <div className="songlist-table-header">
          <div>TITLE</div>
          <div>ARTIST</div>
        </div>

        {songs.length === 0 ? (
          <p className="songlist-empty">No songs found. Time to add some!</p>
        ) : (
          <div style={{ display: 'flex', flexDirection: 'column' }}>
            {songs.map((song) => (
              <div key={song.id} className="songlist-item">
                {/* Okładka / Ikona */}
                <div className="songlist-cover">🎶</div>
                
                {/* Dane utworu */}
                <div className="songlist-info-col">
                  <span className="songlist-title">{song.title}</span>
                </div>
                <div className="songlist-artist">{song.artist}</div>
                
                {/* Przycisk Play */}
                <div className="songlist-play-col">
                  <button className="songlist-play-btn">▶</button>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}