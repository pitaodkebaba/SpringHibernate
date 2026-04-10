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
  const [genres, setGenres] = useState([]);
  const [playlists, setPlaylists] = useState([]);
  const [loading, setLoading] = useState(true);
  const [_, setError] = useState('');

  // Stany logiki widoków
  const [currentView, setCurrentView] = useState('ALL_SONGS'); // 'ALL_SONGS' lub ID playlisty
  const [newPlaylistName, setNewPlaylistName] = useState('');
  const [showNewPlaylistInput, setShowNewPlaylistInput] = useState(false);

  // Stany Admina
  const [isAdmin, setIsAdmin] = useState(false);
  const [showForm, setShowForm] = useState(false);
  const [editingId, setEditingId] = useState(null);
  const [formData, setFormData] = useState({
    title: '', artist: '', album: '', genreName: '', isNewGenre: false
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
      const songRes = await fetch('/api/songs', { headers: { 'Authorization': `Bearer ${token}` } });
      if (songRes.ok) {
        const songData = await songRes.json();
        if (Array.isArray(songData)) setSongs(songData);
        else if (songData && Array.isArray(songData.data)) setSongs(songData.data);
        else if (songData && Array.isArray(songData.content)) setSongs(songData.content);
        else setSongs([]);
      }

      // 2. Pobieramy gatunki
      const genreRes = await fetch('/api/genres', { headers: { 'Authorization': `Bearer ${token}` } });
      if (genreRes.ok) {
        const genreData = await genreRes.json();
        if (Array.isArray(genreData)) setGenres(genreData);
        else if (genreData && Array.isArray(genreData.data)) setGenres(genreData.data);
        else if (genreData && Array.isArray(genreData.content)) setGenres(genreData.content);
        else setGenres([]);
      }

      // 3. NOWE: Pobieranie playlist zalogowanego użytkownika
      const plRes = await fetch('/api/playlists/me', { headers: { 'Authorization': `Bearer ${token}` } });
      if (plRes.ok) {
        const plData = await plRes.json();
        setPlaylists(Array.isArray(plData) ? plData : (plData.data || plData.content || []));
      }
      

    } catch (err) {
      console.error(err);
      setError('Błąd komunikacji z serwerem');
    } finally {
      setLoading(false);
    }
  };
  useEffect(() => { fetchData(); console.log("Pobrane dane:", { songs, genres, playlists }); }, []);

  // --- FUNKCJE PLAYLIST ---

  const handleCreatePlaylist = async (e) => {
    // Sprawdzamy, czy wciśnięto klawisz 'Enter' (kod 13 lub klucz 'Enter')
    if (e.key !== 'Enter' || !newPlaylistName.trim()) return;

    // Zatrzymujemy domyślne zachowanie przeglądarki, np. wysłanie formularza
    e.preventDefault();

    const token = localStorage.getItem('token');
    try {
      // Pamiętamy o pełnym adresie URL!
      const res = await fetch('/api/playlists', {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({ name: newPlaylistName, songIds: [] })
      });

      if (res.ok) {
        setNewPlaylistName('');
        setShowNewPlaylistInput(false);
        fetchData(); // odświeżenie danych po sukcesie
      } else {
        alert("Wystąpił błąd podczas tworzenia playlisty.");
      }
    } catch (err) {
      console.error("Błąd tworzenia playlisty", err);
    }
  };

  const handleAddSongToPlaylist = async (songId, playlistId) => {
    if (!playlistId) return;

    const token = localStorage.getItem('token');
    try {
      const res = await fetch(`/api/playlists/${playlistId}/addSong/${songId}`, {
        method: 'POST',
        headers: { 'Authorization': `Bearer ${token}` }
      });

      if (res.ok) {
        alert("Dodano piosenkę do playlisty!");
        fetchData();
      } else {
        alert("Wystąpił błąd podczas dodawania do playlisty.");
      }
    } catch (err) { console.error(err); }
  };

  const handleDeletePlaylist = async (playlistId) => {
    if (!window.confirm("Na pewno chcesz usunąć tę playlistę?")) return;

    const token = localStorage.getItem('token');
    try {
      const res = await fetch(`/api/playlists/${playlistId}`, {
        method: 'DELETE',
        headers: { 'Authorization': `Bearer ${token}` }
      });
      if (res.ok) {
        if (currentView === playlistId) setCurrentView('ALL_SONGS'); // Wróć do ogólnego widoku jeśli usunęliśmy aktywną
        fetchData();
      }
    } catch (err) { console.error(err); }
  };

  const handleRemoveSongFromPlaylist = async (songId) => {
    if (!window.confirm("Usunąć tę piosenkę z playlisty?")) return;
    
    const token = localStorage.getItem('token');
    try {
      const res = await fetch(`/api/playlists/${currentView}/removeSong/${songId}`, {
        method: 'DELETE',
        headers: { 'Authorization': `Bearer ${token}` }
      });
      if (res.ok) {
        fetchData(); // Odśwież widok po usunięciu
      } else {
        alert("Wystąpił błąd podczas usuwania utworu z playlisty.");
      }
    } catch (err) { console.error(err); }
  };

  // --- FUNKCJE PIOSENEK (Admin) ---

  const handleSaveSong = async (e) => {
    e.preventDefault();
    const token = localStorage.getItem('token');
    const url = editingId ? `/api/songs/${editingId}` : '/api/songs';

    const payload = {
      title: formData.title, artist: formData.artist, album: formData.album, genreName: formData.genreName
    };

    try {
      const res = await fetch(url, {
        method: editingId ? 'PUT' : 'POST',
        headers: { 'Authorization': `Bearer ${token}`, 'Content-Type': 'application/json' },
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
    if (!window.confirm("Usunąć piosenkę z bazy?")) return;
    const token = localStorage.getItem('token');
    await fetch(`/api/songs/${id}`, { method: 'DELETE', headers: { 'Authorization': `Bearer ${token}` } });
    fetchData();
  };

  const startEditing = (song) => {
    setEditingId(song.id);
    setFormData({ title: song.title, artist: song.artist, album: song.album, genreName: song.genre || '', isNewGenre: false });
    setShowForm(true);
  };

  if (loading) return <div className="songlist-container"><h2>Ładowanie...</h2></div>;

  // --- LOGIKA WIDOKU (Co pokazać na środku) ---
  let displayedSongs = [];
  let viewTitle = "Wszystkie Utwory";

  if (currentView === 'ALL_SONGS') {
    displayedSongs = songs;
  } else {
    const activePlaylist = playlists.find(p => p.id === currentView);
    if (activePlaylist) {
      displayedSongs = activePlaylist.songs || [];
      viewTitle = `Playlista: ${activePlaylist.name}`;
    }
  }

  return (
    <div className="app-container">

      {/* --- LEWY PASEK BOCZNY (PLAYLISTY) --- */}
      <div className="sidebar">
        <h1>🎵 MusicApp</h1>

        <div className={`sidebar-menu-item ${currentView === 'ALL_SONGS' ? 'active' : ''}`} onClick={() => setCurrentView('ALL_SONGS')}>
          🏠 Wszystkie Utwory
        </div>

        <div className="playlists-section">
          <div className="playlists-header">
            <span>Moje Playlisty</span>
            <button className="add-playlist-btn" onClick={() => setShowNewPlaylistInput(!showNewPlaylistInput)}>+</button>
          </div>

          {showNewPlaylistInput && (
            <input
              autoFocus
              type="text"
              className="new-playlist-input"
              placeholder="Nazwa i kliknij Enter"
              value={newPlaylistName}
              onChange={(e) => setNewPlaylistName(e.target.value)}
              onKeyDown={handleCreatePlaylist}
            />
          )}

          {playlists.map(playlist => (
            <div key={playlist.id} className={`sidebar-menu-item playlist-item ${currentView === playlist.id ? 'active' : ''}`}>
              <span onClick={() => setCurrentView(playlist.id)} style={{ flex: 1 }}>🎧 {playlist.name}</span>
              <button className="delete-playlist-btn" onClick={(e) => { e.stopPropagation(); handleDeletePlaylist(playlist.id); }}>🗑</button>
            </div>
          ))}
        </div>
      </div>

      {/* --- GŁÓWNA ZAWARTOŚĆ (PIOSENKI I FORMULARZ ADMINA) --- */}
      <div className="main-content">
        <div className="songlist-header" style={{ borderBottom: 'none', background: 'transparent' }}>
          <h2>{viewTitle} {isAdmin && <span className="admin-badge">Admin</span>}</h2>
          <div>
            {isAdmin && currentView === 'ALL_SONGS' && (
              <button className="songlist-action-btn" onClick={() => { setShowForm(!showForm); setEditingId(null); }}>
                {showForm ? 'Anuluj' : '+ Dodaj'}
              </button>
            )}
            <button className="songlist-logout-btn" onClick={() => { localStorage.removeItem('token'); window.location.reload(); }}>Wyloguj</button>
          </div>
        </div>

        <div className="songlist-content" style={{ paddingTop: '0' }}>
          {/* FORMULARZ ADMINA */}
          {showForm && isAdmin && currentView === 'ALL_SONGS' && (
            <form className="songlist-admin-form" onSubmit={handleSaveSong}>
              <h3>{editingId ? 'Edycja' : 'Nowy utwór'}</h3>
              <input type="text" placeholder="Tytuł" value={formData.title} onChange={e => setFormData({ ...formData, title: e.target.value })} required />
              <input type="text" placeholder="Artysta" value={formData.artist} onChange={e => setFormData({ ...formData, artist: e.target.value })} required />
              <input type="text" placeholder="Album" value={formData.album} onChange={e => setFormData({ ...formData, album: e.target.value })} required />

              <div className="genre-selection">
                {!formData.isNewGenre ? (
                  <select value={formData.genreName} onChange={e => { if (e.target.value === "NEW") setFormData({ ...formData, isNewGenre: true, genreName: '' }); else setFormData({ ...formData, genreName: e.target.value }); }} required>
                    <option value="">Wybierz gatunek...</option>
                    {genres.map(g => <option key={g.id} value={g.name}>{g.name}</option>)}
                    <option value="NEW" style={{ fontWeight: 'bold', color: '#1db954' }}>+ Dodaj nowy gatunek</option>
                  </select>
                ) : (
                  <div style={{ display: 'flex', gap: '10px' }}>
                    <input type="text" placeholder="Nazwa nowego gatunku" value={formData.genreName} onChange={e => setFormData({ ...formData, genreName: e.target.value })} required />
                    <button type="button" onClick={() => setFormData({ ...formData, isNewGenre: false, genreName: '' })}>Wróć</button>
                  </div>
                )}
              </div>

              <button type="submit" className="songlist-submit-btn">Zapisz utwór</button>
            </form>
          )}

          {/* TABELA PIOSENEK */}
          <div className="song-table">
            <div className="songlist-table-header">
              <div style={{ flex: 2 }}>TYTUŁ / ALBUM</div>
              <div style={{ flex: 1 }}>ARTYSTA</div>
              <div style={{ flex: 1 }}>GATUNEK</div>
              <div style={{ flex: 1, textAlign: 'right' }}>AKCJE</div>
            </div>

            {displayedSongs.length === 0 ? (
              <p style={{ color: '#b3b3b3', paddingLeft: '90px' }}>Brak utworów w tym widoku.</p>
            ) : (
              displayedSongs.map(song => (
                <div key={song.id} className="songlist-item">
                  <div className="songlist-cover">🎶</div>
                  <div style={{ flex: 2, display: 'flex', flexDirection: 'column' }}>
                    <span className="songlist-title">{song.title}</span>
                    <span style={{ fontSize: '12px', color: '#b3b3b3' }}>{song.album}</span>
                  </div>
                  <div style={{ flex: 1 }}>{song.artist}</div>
                  <div style={{ flex: 1, color: '#1db954' }}>
                    {song.genre && typeof song.genre === 'object' ? song.genre.name : (song.genre || 'Brak')}
                  </div>

                  <div className="songlist-admin-actions" style={{ alignItems: 'center' }}>
                    
                    {/* WIDOK: WSZYSTKIE UTWORY */}
                    {currentView === 'ALL_SONGS' && (
                      <>
                        {playlists.length > 0 && (
                          <select className="add-to-playlist-select" onChange={(e) => { handleAddSongToPlaylist(song.id, e.target.value); e.target.value = ""; }}>
                            <option value="">+ Do playlisty</option>
                            {playlists.map(pl => <option key={pl.id} value={pl.id}>{pl.name}</option>)}
                          </select>
                        )}
                        
                        {/* Akcje Admina w widoku wszystkich utworów */}
                        {isAdmin && (
                          <>
                            <button className="edit-btn" title="Edytuj" onClick={() => startEditing(song)}>✎</button>
                            <button className="delete-btn" title="Usuń z bazy" onClick={() => handleDelete(song.id)}>🗑</button>
                          </>
                        )}
                      </>
                    )}

                    {/* WIDOK: WNĘTRZE PLAYLISTY */}
                    {currentView !== 'ALL_SONGS' && (
                       <button 
                         className="delete-btn" 
                         title="Usuń z playlisty" 
                         onClick={() => handleRemoveSongFromPlaylist(song.id)}
                         style={{fontSize: '16px'}}
                       >
                         ❌
                       </button>
                    )}

                  </div>
                </div>
              ))
            )}
          </div>
        </div>
      </div>
    </div>
  );
}