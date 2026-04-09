import { useState, useEffect } from 'react';
import './SongList.css';

const parseJwt = (token) => {
  try {
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(window.atob(base64).split('').map(c => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2)).join(''));
    return JSON.parse(jsonPayload);
  } catch (e) { return e.message; }
};

export default function SongList() {
  const [songs, setSongs] = useState([]);
  const [genres, setGenres] = useState([]); // Lista gatunków z bazy
  const [loading, setLoading] = useState(true);
  const [_, setError] = useState('');
  
  const [isAdmin, setIsAdmin] = useState(false);
  const [showForm, setShowForm] = useState(false);
  const [editingId, setEditingId] = useState(null);
  
  // Nowa struktura formData zgodna z Twoim modelem
  const [formData, setFormData] = useState({ 
    title: '', 
    artist: '', 
    album: '', 
    genreName: '', // Przesyłamy nazwę gatunku
    isNewGenre: false 
  });

  const fetchData = async () => {
    const token = localStorage.getItem('token');
    if (!token) return;

    const decoded = parseJwt(token);
    if (decoded && (decoded.role === 'ADMIN' || (decoded.authorities && decoded.authorities.includes('ADMIN')))) {
      setIsAdmin(true);
    }

    try {
      // 1. Pobieramy piosenki
      const songRes = await fetch('/api/songs', {
        headers: { 'Authorization': `Bearer ${token}` }
      });
      if (songRes.ok) {
         const songData = await songRes.json();
         // Zabezpieczenie dla piosenek
         if (Array.isArray(songData)) setSongs(songData);
         else if (songData && Array.isArray(songData.data)) setSongs(songData.data);
         else if (songData && Array.isArray(songData.content)) setSongs(songData.content);
         else setSongs([]); 
      }

      // 2. Pobieramy gatunki
      const genreRes = await fetch('/api/genres', {
        headers: { 'Authorization': `Bearer ${token}` }
      });
      if (genreRes.ok) {
        const genreData = await genreRes.json();
        if (Array.isArray(genreData)) {
          setGenres(genreData);
        } else if (genreData && Array.isArray(genreData.data)) {
          setGenres(genreData.data);
        } else if (genreData && Array.isArray(genreData.content)) {
          setGenres(genreData.content);
        } else {
          console.error("Gatunki nie są tablicą!", genreData);
          setGenres([]);
        }
      }
    } catch (err) {
      console.error(err);
      setError('Błąd komunikacji z serwerem');
    } finally {
      setLoading(false);
    }
  };
  useEffect(() => { fetchData(); }, []);

  const handleSaveSong = async (e) => {
    e.preventDefault();
    const token = localStorage.getItem('token');
    const url = editingId ? `/api/songs/${editingId}` : '/api/songs';
    
    // Przygotowujemy obiekt do wysłania (Backend powinien obsłużyć genreName)
    const payload = {
      title: formData.title,
      artist: formData.artist,
      album: formData.album,
      genreName: formData.genreName
    };

    try {
      const res = await fetch(url, {
        method: editingId ? 'PUT' : 'POST',
        headers: { 
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json' 
        },
        body: JSON.stringify(payload)
      });

      if (res.ok) {
        setShowForm(false);
        setEditingId(null);
        setFormData({ title: '', artist: '', album: '', genreName: '', isNewGenre: false });
        fetchData();
      }
    } catch (err) { console.error(err); }
  };

  const handleDelete = async (id) => {
    if (!window.confirm("Usunąć?")) return;
    const token = localStorage.getItem('token');
    await fetch(`/api/songs/${id}`, {
      method: 'DELETE',
      headers: { 'Authorization': `Bearer ${token}` }
    });
    fetchData();
  };

  const startEditing = (song) => {
    setEditingId(song.id);
    setFormData({ 
      title: song.title, 
      artist: song.artist, 
      album: song.album, 
      genreName: song.genre?.name || '', 
      isNewGenre: false 
    });
    setShowForm(true);
  };

  if (loading) return <div className="songlist-container"><h2>Ładowanie...</h2></div>;

  return (
    <div className="songlist-container">
      <div className="songlist-header">
        <h1>🎵 MusicBrowser {isAdmin && <span className="admin-badge">Admin</span>}</h1>
        <div>
          {isAdmin && (
            <button className="songlist-action-btn" onClick={() => { setShowForm(!showForm); setEditingId(null); }}>
              {showForm ? 'Anuluj' : '+ Dodaj'}
            </button>
          )}
          <button className="songlist-logout-btn" onClick={() => { localStorage.removeItem('token'); window.location.reload(); }}>Wyloguj</button>
        </div>
      </div>

      <div className="songlist-content">
        {showForm && isAdmin && (
          <form className="songlist-admin-form" onSubmit={handleSaveSong}>
            <h3>{editingId ? 'Edycja' : 'Nowy utwór'}</h3>
            <input type="text" placeholder="Tytuł" value={formData.title} onChange={e => setFormData({...formData, title: e.target.value})} required />
            <input type="text" placeholder="Artysta" value={formData.artist} onChange={e => setFormData({...formData, artist: e.target.value})} required />
            <input type="text" placeholder="Album" value={formData.album} onChange={e => setFormData({...formData, album: e.target.value})} required />
            
            <div className="genre-selection">
              {!formData.isNewGenre ? (
                <select 
                  value={formData.genreName} 
                  onChange={e => {
                    if (e.target.value === "NEW") setFormData({...formData, isNewGenre: true, genreName: ''});
                    else setFormData({...formData, genreName: e.target.value});
                  }}
                  required
                >
                  <option value="">Wybierz gatunek...</option>
                  {genres.map(g => <option key={g.id} value={g.name}>{g.name}</option>)}
                  <option value="NEW" style={{fontWeight: 'bold', color: '#1db954'}}>+ Dodaj nowy gatunek</option>
                </select>
              ) : (
                <div style={{display: 'flex', gap: '10px'}}>
                  <input type="text" placeholder="Nazwa nowego gatunku" value={formData.genreName} onChange={e => setFormData({...formData, genreName: e.target.value})} required />
                  <button type="button" onClick={() => setFormData({...formData, isNewGenre: false, genreName: ''})}>Wróć</button>
                </div>
              )}
            </div>

            <button type="submit" className="songlist-submit-btn">Zapisz utwór</button>
          </form>
        )}

        <div className="song-table">
          <div className="songlist-table-header">
            <div style={{flex: 2}}>TYTUŁ / ALBUM</div>
            <div style={{flex: 1}}>ARTYSTA</div>
            <div style={{flex: 1}}>GATUNEK</div>
            {isAdmin && <div style={{flex: 1, textAlign: 'right'}}>AKCJE</div>}
          </div>

          {songs.map(song => (
            <div key={song.id} className="songlist-item">
              <div className="songlist-cover">🎶</div>
              <div style={{flex: 2, display: 'flex', flexDirection: 'column'}}>
                <span className="songlist-title">{song.title}</span>
                <span style={{fontSize: '12px', color: '#b3b3b3'}}>{song.album}</span>
              </div>
              <div style={{flex: 1}}>{song.artist}</div>
              <div style={{flex: 1, color: '#1db954'}}>{song.genre || 'Brak'}</div>
              
              {isAdmin && (
                <div className="songlist-admin-actions">
                  <button className="edit-btn" onClick={() => startEditing(song)}>✎</button>
                  <button className="delete-btn" onClick={() => handleDelete(song.id)}>🗑</button>
                </div>
              )}
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}