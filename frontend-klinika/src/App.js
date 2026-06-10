import React, { useState, useEffect } from 'react';
import './App.css'; // Importujemy style

const API_BASE_URL = 'http://localhost:8080/api';

function App() {
    const [aktywnaZakladka, setAktywnaZakladka] = useState('uzytkownicy');

    return (
        <div className="app-container">
            <h1 className="header-title">🐾 Panel Kliniki Weterynaryjnej</h1>

            <div className="navbar">
                <button
                    className={`nav-btn ${aktywnaZakladka === 'uzytkownicy' ? 'active' : ''}`}
                    onClick={() => setAktywnaZakladka('uzytkownicy')}
                >
                    Użytkownicy
                </button>
                <button
                    className={`nav-btn ${aktywnaZakladka === 'zwierzeta' ? 'active' : ''}`}
                    onClick={() => setAktywnaZakladka('zwierzeta')}
                >
                    Zwierzęta
                </button>
                <button
                    className={`nav-btn ${aktywnaZakladka === 'wizyty' ? 'active' : ''}`}
                    onClick={() => setAktywnaZakladka('wizyty')}
                >
                    Wizyty
                </button>
            </div>

            <div className="view-section">
                {aktywnaZakladka === 'uzytkownicy' && <UzytkownicyWidok />}
                {aktywnaZakladka === 'zwierzeta' && <ZwierzetaWidok />}
                {aktywnaZakladka === 'wizyty' && <WizytyWidok />}
            </div>
        </div>
    );
}

// --- WIDOK UŻYTKOWNIKÓW ---
function UzytkownicyWidok() {
    const [uzytkownicy, setUzytkownicy] = useState([]);
    const [nowyKlient, setNowyKlient] = useState({ email: '', password: '', nrTelefonu: '' });

    const pobierzUzytkownikow = () => {
        fetch(`${API_BASE_URL}/uzytkownicy`)
            .then(res => res.json())
            .then(data => setUzytkownicy(data))
            .catch(err => console.error("Błąd pobierania:", err));
    };

    useEffect(() => {
        pobierzUzytkownikow();
    }, []);

    const dodajKlienta = (e) => {
        e.preventDefault();
        fetch(`${API_BASE_URL}/uzytkownicy/klient`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(nowyKlient)
        })
            .then(async res => {
                if (res.ok) {
                    alert('Klient dodany pomyślnie!');
                    pobierzUzytkownikow();
                    setNowyKlient({ email: '', password: '', nrTelefonu: '' });
                } else {
                    const err = await res.text();
                    alert('Błąd dodawania klienta: ' + err);
                }
            });
    };

    return (
        <div>
            <h2 className="section-title">Zarządzanie Użytkownikami</h2>

            <form onSubmit={dodajKlienta} className="modern-form">
                <input
                    type="email"
                    className="form-input"
                    placeholder="E-mail"
                    value={nowyKlient.email}
                    onChange={e => setNowyKlient({...nowyKlient, email: e.target.value})}
                    required
                />
                <input
                    type="password"
                    className="form-input"
                    placeholder="Hasło"
                    value={nowyKlient.password}
                    onChange={e => setNowyKlient({...nowyKlient, password: e.target.value})}
                    required
                />
                <input
                    type="text"
                    className="form-input"
                    placeholder="Nr telefonu"
                    value={nowyKlient.nrTelefonu}
                    onChange={e => setNowyKlient({...nowyKlient, nrTelefonu: e.target.value})}
                />
                <button type="submit" className="submit-btn">+ Dodaj Klienta</button>
            </form>

            <h3 className="subsection-title">Zarejestrowani w systemie</h3>
            <ul className="item-list">
                {uzytkownicy.map((u, index) => (
                    <li key={index} className="list-item">
                        <span className="badge">{u.rola}</span>
                        <strong>{u.email}</strong>
                        <span style={{color: '#94a3b8', fontSize: '13px'}}>(ID: {u.id})</span>
                    </li>
                ))}
                {uzytkownicy.length === 0 && (
                    <p style={{color: '#94a3b8'}}>Brak użytkowników do wyświetlenia.</p>
                )}
            </ul>
        </div>
    );
}

// --- WIDOK ZWIERZĄT ---
function ZwierzetaWidok() {
    const [wlascicielId, setWlascicielId] = useState('');
    const [noweZwierze, setNoweZwierze] = useState({ imie: '', gatunek: '', dataurodzenia: '' });

    const zarejestrujZwierze = (e) => {
        e.preventDefault();
        fetch(`${API_BASE_URL}/zwierzeta?wlascicielId=${wlascicielId}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(noweZwierze)
        })
            .then(async res => {
                if (res.ok) {
                    alert('Zwierzę zarejestrowane!');
                    setNoweZwierze({ imie: '', gatunek: '', dataurodzenia: '' });
                    setWlascicielId('');
                } else {
                    const err = await res.text();
                    alert('Wystąpił błąd podczas rejestracji: ' + err);
                }
            });
    };

    return (
        <div>
            <h2 className="section-title">Rejestracja Nowego Pacjenta</h2>
            <form onSubmit={zarejestrujZwierze} className="modern-form">
                <input
                    className="form-input"
                    type="number"
                    placeholder="ID Właściciela"
                    value={wlascicielId}
                    onChange={e => setWlascicielId(e.target.value)}
                    required
                />
                <input
                    className="form-input"
                    placeholder="Imię zwierzęcia"
                    value={noweZwierze.imie}
                    onChange={e => setNoweZwierze({...noweZwierze, imie: e.target.value})}
                    required
                />
                <input
                    className="form-input"
                    placeholder="Gatunek (np. Pies, Kot)"
                    value={noweZwierze.gatunek}
                    onChange={e => setNoweZwierze({...noweZwierze, gatunek: e.target.value})}
                    required
                />
                <input
                    className="form-input"
                    type="date"
                    placeholder="Data urodzenia"
                    value={noweZwierze.dataurodzenia}
                    onChange={e => setNoweZwierze({...noweZwierze, dataurodzenia: e.target.value})}
                />
                <button type="submit" className="submit-btn">+ Zarejestruj Zwierzę</button>
            </form>
        </div>
    );
}

// --- WIDOK WIZYT ---
function WizytyWidok() {
    const [nowaWizyta, setNowaWizyta] = useState({
        data: '',
        zwierzeId: '',
        vetId: ''
    });

    const umowWizyte = (e) => {
        e.preventDefault();
        fetch(`${API_BASE_URL}/wizyty/umow`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(nowaWizyta)
        })
            .then(async res => {
                if (res.ok) {
                    alert('Wizyta umówiona pomyślnie!');
                    setNowaWizyta({ data: '', zwierzeId: '', vetId: '' });
                }
                else {
                    const err = await res.text();
                    alert('Błąd przy umawianiu wizyty: ' + err);
                }
            });
    };

    return (
        <div>
            <h2 className="section-title">Umawianie Wizyty</h2>
            <form onSubmit={umowWizyte} className="modern-form">
                <input
                    className="form-input"
                    type="datetime-local"
                    value={nowaWizyta.data}
                    onChange={e => setNowaWizyta({...nowaWizyta, data: e.target.value})}
                    required
                />
                <input
                    className="form-input"
                    type="number"
                    placeholder="ID Zwierzęcia"
                    value={nowaWizyta.zwierzeId}
                    onChange={e => setNowaWizyta({...nowaWizyta, zwierzeId: e.target.value})}
                    required
                />
                <input
                    className="form-input"
                    type="number"
                    placeholder="ID Weterynarza (vetId)"
                    value={nowaWizyta.vetId}
                    onChange={e => setNowaWizyta({...nowaWizyta, vetId: e.target.value})}
                    required
                />
                <button type="submit" className="submit-btn">📅 Zapisz do kalendarza</button>
            </form>
        </div>
    );
}

export default App;