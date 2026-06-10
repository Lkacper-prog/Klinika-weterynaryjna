import React, { useState, useEffect } from 'react';
import './App.css';

const API_BASE_URL = 'http://localhost:8080/api';

function App() {
    const [aktywnaZakladka, setAktywnaZakladka] = useState('lekarz');
    const [toasts, setToasts] = useState([]);

    const addToast = (message, type = 'success') => {
        const id = Date.now();
        setToasts(prev => [...prev, { id, message, type }]);
        setTimeout(() => {
            setToasts(prev => prev.filter(t => t.id !== id));
        }, 4000);
    };

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
                    Wizyty (Kalendarz)
                </button>
                <button
                    className={`nav-btn ${aktywnaZakladka === 'lekarz' ? 'active' : ''}`}
                    onClick={() => setAktywnaZakladka('lekarz')}
                    style={{
                        borderColor: '#10b981',
                        color: aktywnaZakladka === 'lekarz' ? '#fff' : '#10b981',
                        backgroundColor: aktywnaZakladka === 'lekarz' ? '#10b981' : 'transparent'
                    }}
                >
                    👨‍⚕️ Panel Lekarza
                </button>
            </div>

            <div className="view-section">
                {aktywnaZakladka === 'uzytkownicy' && <UzytkownicyWidok addToast={addToast} />}
                {aktywnaZakladka === 'zwierzeta' && <ZwierzetaWidok addToast={addToast} />}
                {aktywnaZakladka === 'wizyty' && <WizytyWidok addToast={addToast} />}
                {aktywnaZakladka === 'lekarz' && <LekarzWidok addToast={addToast} />}
            </div>

            <div className="toast-container">
                {toasts.map(t => (
                    <div key={t.id} className={`toast ${t.type}`}>
                        {t.type === 'success' ? '✅ ' : '❌ '} {t.message}
                    </div>
                ))}
            </div>
        </div>
    );
}

// --- WIDOK UŻYTKOWNIKÓW ---
function UzytkownicyWidok({ addToast }) {
    const [uzytkownicy, setUzytkownicy] = useState([]);
    const [nowyKlient, setNowyKlient] = useState({ email: '', password: '', nrTelefonu: '' });
    const [isLoading, setIsLoading] = useState(false);

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
        setIsLoading(true);
        fetch(`${API_BASE_URL}/uzytkownicy/klient`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(nowyKlient)
        })
            .then(async res => {
                if (res.ok) {
                    addToast('Klient został dodany pomyślnie!', 'success');
                    pobierzUzytkownikow();
                    setNowyKlient({ email: '', password: '', nrTelefonu: '' });
                } else {
                    const err = await res.text();
                    addToast(`Błąd: ${err}`, 'error');
                }
            })
            .catch(() => addToast('Błąd połączenia z serwerem!', 'error'))
            .finally(() => setIsLoading(false));
    };

    return (
        <div>
            <h2 className="section-title">Zarządzanie Użytkownikami</h2>
            <form onSubmit={dodajKlienta} className="modern-form">
                <input type="email" className="form-input" placeholder="E-mail logowania" value={nowyKlient.email} onChange={e => setNowyKlient({...nowyKlient, email: e.target.value})} required />
                <input type="password" className="form-input" placeholder="Hasło" value={nowyKlient.password} onChange={e => setNowyKlient({...nowyKlient, password: e.target.value})} required />
                <input type="text" className="form-input" placeholder="Nr telefonu" value={nowyKlient.nrTelefonu} onChange={e => setNowyKlient({...nowyKlient, nrTelefonu: e.target.value})} />
                <button type="submit" className="submit-btn" disabled={isLoading}>
                    {isLoading ? <span className="loader"></span> : '+ Dodaj Klienta'}
                </button>
            </form>

            <h3 className="subsection-title">Zarejestrowani w systemie ({uzytkownicy.length})</h3>
            <ul className="item-list">
                {uzytkownicy.map((u, index) => (
                    <li key={index} className="list-item">
                        <span className="badge">{u.rola}</span>
                        <strong>{u.email}</strong>
                        <span style={{color: '#94a3b8', fontSize: '13px'}}>(ID: {u.id})</span>
                    </li>
                ))}
            </ul>
        </div>
    );
}

// --- WIDOK ZWIERZĄT ---
// --- WIDOK ZWIERZĄT ---
function ZwierzetaWidok({ addToast }) {
    // Stany dla rejestracji
    const [wlascicielId, setWlascicielId] = useState('');
    const [noweZwierze, setNoweZwierze] = useState({ imie: '', gatunek: '', dataurodzenia: '' });
    const [isLoadingZapis, setIsLoadingZapis] = useState(false);

    // Stany dla wyszukiwarki pacjentów
    const [szukaneId, setSzukaneId] = useState('');
    const [znalezioneZwierze, setZnalezioneZwierze] = useState(null);
    const [raportZwierzecia, setRaportZwierzecia] = useState(null);
    const [isLoadingSzukaj, setIsLoadingSzukaj] = useState(false);

    const zarejestrujZwierze = (e) => {
        e.preventDefault();
        setIsLoadingZapis(true);
        fetch(`${API_BASE_URL}/zwierzeta?wlascicielId=${wlascicielId}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(noweZwierze)
        })
            .then(async res => {
                if (res.ok) {
                    addToast('Pacjent (Zwierzę) dodany do systemu!', 'success');
                    setNoweZwierze({ imie: '', gatunek: '', dataurodzenia: '' });
                    setWlascicielId('');
                } else {
                    const err = await res.text();
                    addToast(`Błąd rejestracji: ${err}`, 'error');
                }
            })
            .finally(() => setIsLoadingZapis(false));
    };

    const szukajPacjenta = (e) => {
        e.preventDefault();
        setIsLoadingSzukaj(true);
        setZnalezioneZwierze(null);
        setRaportZwierzecia(null);

        fetch(`${API_BASE_URL}/zwierzeta/${szukaneId}`)
            .then(res => {
                if (!res.ok) throw new Error('Nie znaleziono pacjenta o podanym ID');
                return res.json();
            })
            .then(data => {
                setZnalezioneZwierze(data);
                addToast('Pobrano kartę pacjenta!', 'success');
            })
            .catch(err => addToast(err.message, 'error'))
            .finally(() => setIsLoadingSzukaj(false));
    };

    const generujRaport = () => {
        fetch(`${API_BASE_URL}/zwierzeta/${szukaneId}/raport`)
            .then(res => res.json())
            .then(data => {
                setRaportZwierzecia(data);
                addToast('Raport wygenerowany pomyślnie!', 'success');
            })
            .catch(() => addToast('Błąd generowania raportu!', 'error'));
    };

    return (
        <div style={{ display: 'flex', gap: '40px', flexWrap: 'wrap' }}>

            {/* Lewa kolumna: Rejestracja Pacjenta */}
            <div style={{ flex: 1, minWidth: '300px' }}>
                <h2 className="section-title">Rejestracja Pacjenta</h2>
                <form onSubmit={zarejestrujZwierze} className="modern-form">
                    <input className="form-input" type="number" placeholder="ID Właściciela (np. 2)" value={wlascicielId} onChange={e => setWlascicielId(e.target.value)} required />
                    <input className="form-input" placeholder="Imię zwierzęcia" value={noweZwierze.imie} onChange={e => setNoweZwierze({...noweZwierze, imie: e.target.value})} required />
                    <input className="form-input" placeholder="Gatunek (np. Pies)" value={noweZwierze.gatunek} onChange={e => setNoweZwierze({...noweZwierze, gatunek: e.target.value})} required />
                    <input className="form-input" type="date" value={noweZwierze.dataurodzenia} onChange={e => setNoweZwierze({...noweZwierze, dataurodzenia: e.target.value})} required />
                    <button type="submit" className="submit-btn" disabled={isLoadingZapis}>
                        {isLoadingZapis ? <span className="loader"></span> : '+ Zarejestruj Zwierzę'}
                    </button>
                </form>
            </div>

            {/* Prawa kolumna: Wyszukiwarka i Karta Pacjenta */}
            <div style={{ flex: 1, minWidth: '300px' }}>
                <h2 className="section-title">Wyszukiwarka i Karta Pacjenta</h2>
                <form onSubmit={szukajPacjenta} style={{ display: 'flex', gap: '10px' }}>
                    <input className="form-input" type="number" style={{ flex: 1 }} placeholder="Wpisz ID Zwierzęcia (np. 1)" value={szukaneId} onChange={e => setSzukaneId(e.target.value)} required />
                    <button type="submit" className="submit-btn" style={{ margin: 0, padding: '0 20px' }} disabled={isLoadingSzukaj}>
                        {isLoadingSzukaj ? <span className="loader"></span> : 'Szukaj'}
                    </button>
                </form>

                {/* Sekcja wyświetlająca dane po znalezieniu pacjenta */}
                {znalezioneZwierze && (
                    <div className="info-card">
                        <h3>🐾 {znalezioneZwierze.imie} <span className="badge secondary">{znalezioneZwierze.gatunek}</span></h3>
                        <p><strong>Właściciel:</strong> {znalezioneZwierze.wlasciciel ? znalezioneZwierze.wlasciciel.email : 'Brak przypisania'}</p>
                        <p><strong>Data urodzenia:</strong> {znalezioneZwierze.dataurodzenia}</p>

                        <button onClick={generujRaport} className="submit-btn" style={{ backgroundColor: '#8b5cf6', width: '100%', marginTop: '15px' }}>
                            📄 Generuj Pełny Raport Leczenia
                        </button>

                        {/* Sekcja wyświetlająca wygenerowany raport */}
                        {raportZwierzecia && (
                            <div style={{ marginTop: '20px' }}>
                                <strong>Podsumowanie leczenia (JSON z serwera):</strong>
                                <pre className="json-box">{JSON.stringify(raportZwierzecia, null, 2)}</pre>
                            </div>
                        )}
                    </div>
                )}
            </div>

        </div>
    );
}

// --- WIDOK WIZYT ---
function WizytyWidok({ addToast }) {
    const [nowaWizyta, setNowaWizyta] = useState({ data: '', zwierzeId: '', vetId: '' });
    const [isLoading, setIsLoading] = useState(false);
    const [wizyty, setWizyty] = useState([]);
    const [isLoadingLista, setIsLoadingLista] = useState(false);

    const pobierzWizyty = () => {
        setIsLoadingLista(true);
        fetch(`${API_BASE_URL}/wizyty`)
            .then(res => {
                if (!res.ok) throw new Error('Nie udało się pobrać kalendarza');
                return res.json();
            })
            .then(data => setWizyty(data))
            .catch(err => console.error("Błąd pobierania wizyt:", err))
            .finally(() => setIsLoadingLista(false));
    };

    useEffect(() => {
        pobierzWizyty();
    }, []);

    const umowWizyte = (e) => {
        e.preventDefault();
        setIsLoading(true);

        let sformatowanaData = nowaWizyta.data;
        if (sformatowanaData.length === 16) {
            sformatowanaData += ':00';
        }
        const obiektDoWyslania = { ...nowaWizyta, data: sformatowanaData };

        fetch(`${API_BASE_URL}/wizyty/umow`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(obiektDoWyslania)
        })
            .then(async res => {
                if (res.ok) {
                    addToast('Wizyta pomyślnie zapisana w kalendarzu!', 'success');
                    setNowaWizyta({ data: '', zwierzeId: '', vetId: '' });
                    pobierzWizyty();
                } else {
                    const err = await res.text();
                    addToast(`Błąd: ${err || 'Konflikt terminów lub niepoprawne ID!'}`, 'error');
                }
            })
            .catch(() => addToast('Błąd połączenia z serwerem!', 'error'))
            .finally(() => setIsLoading(false));
    };

    const formatujDate = (dataString) => {
        if (!dataString) return '';
        const opcje = { year: 'numeric', month: 'long', day: 'numeric', hour: '2-digit', minute: '2-digit' };
        return new Date(dataString).toLocaleDateString('pl-PL', opcje);
    };

    return (
        <div style={{ display: 'flex', gap: '40px', flexWrap: 'wrap' }}>
            <div style={{ flex: 1, minWidth: '300px' }}>
                <h2 className="section-title">Umawianie Wizyty</h2>
                <form onSubmit={umowWizyte} className="modern-form">
                    <input className="form-input" type="datetime-local" value={nowaWizyta.data} onChange={e => setNowaWizyta({...nowaWizyta, data: e.target.value})} required />
                    <input className="form-input" type="number" placeholder="ID Zwierzęcia (np. 1)" value={nowaWizyta.zwierzeId} onChange={e => setNowaWizyta({...nowaWizyta, zwierzeId: e.target.value})} required />
                    <input className="form-input" type="number" placeholder="ID Weterynarza (np. 1)" value={nowaWizyta.vetId} onChange={e => setNowaWizyta({...nowaWizyta, vetId: e.target.value})} required />
                    <button type="submit" className="submit-btn" disabled={isLoading}>
                        {isLoading ? <span className="loader"></span> : '📅 Zapisz do kalendarza'}
                    </button>
                </form>
            </div>

            <div style={{ flex: 1, minWidth: '350px' }}>
                <h2 className="section-title">Zaplanowane Wizyty ({wizyty.length})</h2>
                {isLoadingLista ? (
                    <p style={{ color: '#64748b' }}>⏳ Ładowanie kalendarza...</p>
                ) : (
                    <ul className="item-list">
                        {wizyty.map((w, index) => (
                            <li key={index} className="list-item" style={{ flexDirection: 'column', alignItems: 'flex-start' }}>
                                <div style={{ display: 'flex', justifyContent: 'space-between', width: '100%', marginBottom: '8px' }}>
                                    <span className="badge">{w.status || 'ZAPLANOWANA'}</span>
                                    <strong style={{ color: '#0ea5e9' }}>{formatujDate(w.dataczas)}</strong>
                                </div>
                                <div style={{ fontSize: '14px', color: '#475569' }}>
                                    <p style={{ margin: '4px 0' }}>🐾 <strong>Pacjent:</strong> {w.zwierze ? `${w.zwierze.imie} (${w.zwierze.gatunek})` : `ID: ${w.zwierzeId}`}</p>
                                    <p style={{ margin: '4px 0' }}>👨‍⚕️ <strong>Weterynarz:</strong> {w.weterynarz ? w.weterynarz.email : `ID: ${w.weterynarzId}`}</p>
                                </div>
                            </li>
                        ))}
                        {wizyty.length === 0 && (
                            <p style={{ color: '#94a3b8' }}>Brak zaplanowanych wizyt. Kalendarz jest pusty.</p>
                        )}
                    </ul>
                )}
            </div>
        </div>
    );
}

// --- NOWY WIDOK: PANEL LEKARZA Z ROZWIJANYM FORMULARZEM LECZENIA ---
function LekarzWidok({ addToast }) {
    const [wizyty, setWizyty] = useState([]);
    const [wybranaWizytaId, setWybranaWizytaId] = useState(null); // Przechowuje ID wizyty aktualnie badanej
    const [leczenie, setLeczenie] = useState({ nazwa: 'Szczepienie podstawowe', opis: '', koszt: '50' });

    const pobierzWizyty = () => {
        fetch(`${API_BASE_URL}/wizyty`)
            .then(res => res.json())
            .then(data => setWizyty(data))
            .catch(err => console.error("Błąd pobierania:", err));
    };

    useEffect(() => {
        pobierzWizyty();
    }, []);

    const zakonczBadanie = (e) => {
        e.preventDefault();

        // ZABEZPIECZENIE TYPU: Zmieniamy tekst z inputa na prawdziwą liczbę zmiennoprzecinkową
        const daneDoWyslania = {
            ...leczenie,
            koszt: parseFloat(leczenie.koszt)
        };

        fetch(`${API_BASE_URL}/wizyty/${wybranaWizytaId}/zrealizuj`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' }, // Mówimy serwerowi, że wysyłamy JSON-a
            body: JSON.stringify(daneDoWyslania) // Pakujemy dane do Body zapytania
        })
            .then(async res => {
                if (res.ok) {
                    addToast('Leczenie zapisane. Wizyta pomyślnie zrealizowana!', 'success');
                    setWybranaWizytaId(null);
                    setLeczenie({ nazwa: 'Szczepienie podstawowe', opis: '', koszt: '50' });
                    pobierzWizyty(); // Odświeżamy listę po zmianie statusu
                } else {
                    const errText = await res.text();
                    addToast(`Błąd: ${errText}`, 'error');
                }
            })
            .catch(() => addToast('Błąd połączenia z serwerem!', 'error'));
    };

    const formatujDate = (dataString) => {
        if (!dataString) return '';
        return new Date(dataString).toLocaleDateString('pl-PL', { year: 'numeric', month: 'long', day: 'numeric', hour: '2-digit', minute: '2-digit' });
    };

    // Filtrujemy wizyty. Backend ustawia status jako 'ZAKONCZONA' po realizacji.
    const oczekujace = wizyty.filter(w => w.status !== 'ZAKONCZONA');
    const zakonczenie = wizyty.filter(w => w.status === 'ZAKONCZONA');

    return (
        <div>
            <h2 className="section-title">👨‍⚕️ Panel Gabinetu Lekarskiego</h2>
            <p style={{ color: '#64748b', marginBottom: '30px' }}>Wybierz pacjenta, zbadaj go i wypełnij kartę leczenia.</p>

            <h3 className="subsection-title">⏳ Pacjenci w kolejce ({oczekujace.length})</h3>
            <ul className="item-list">
                {oczekujace.map((w) => (
                    <li key={w.id} className="list-item" style={{ flexDirection: 'column', alignItems: 'stretch' }}>
                        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                            <div>
                                <strong>{formatujDate(w.dataczas)}</strong> - Pacjent: <span style={{color:'#0ea5e9', fontWeight:'bold'}}>{w.zwierze ? w.zwierze.imie : w.zwierzeId}</span>
                            </div>
                            <button
                                onClick={() => setWybranaWizytaId(wybranaWizytaId === w.id ? null : w.id)}
                                className="submit-btn" style={{ margin: 0, padding: '8px 15px', backgroundColor: '#10b981' }}
                            >
                                {wybranaWizytaId === w.id ? '✖ Zamknij' : '🩺 Zbadaj Pacjenta'}
                            </button>
                        </div>

                        {/* FORMULARZ LECZENIA - rozwija się tylko pod aktualnie badanym pacjentem */}
                        {wybranaWizytaId === w.id && (
                            <form onSubmit={zakonczBadanie} className="modern-form" style={{ marginTop: '20px', padding: '20px', background: '#f8fafc', borderRadius: '8px', maxWidth: '100%' }}>
                                <h4 style={{ margin: '0 0 15px 0', color: '#475569' }}>Karta wizyty medycznej:</h4>

                                <label style={{ fontSize: '14px', fontWeight: 'bold' }}>Wykonany zabieg / Procedura:</label>
                                <select className="form-input" style={{ width: '100%', marginBottom: '10px' }} value={leczenie.nazwa} onChange={e => setLeczenie({...leczenie, nazwa: e.target.value})}>
                                    <option value="Szczepienie podstawowe">Szczepienie podstawowe (50 zł)</option>
                                    <option value="Kontrola ogólna">Kontrola ogólna (80 zł)</option>
                                    <option value="Kastracja / Sterylizacja">Kastracja / Sterylizacja (250 zł)</option>
                                    <option value="Badanie krwi profil rozszerzony">Badanie krwi profil rozszerzony (120 zł)</option>
                                    <option value="Zabieg chirurgiczny pilny">Zabieg chirurgiczny pilny (450 zł)</option>
                                </select>

                                <label style={{ fontSize: '14px', fontWeight: 'bold' }}>Cena zabiegu (zł):</label>
                                <input className="form-input" type="number" step="0.01" value={leczenie.koszt} onChange={e => setLeczenie({...leczenie, koszt: e.target.value})} required />

                                <label style={{ fontSize: '14px', fontWeight: 'bold', marginTop: '10px', display: 'block' }}>Zalecenia i opis objawów:</label>
                                <textarea className="form-input" style={{ fontFamily: 'inherit', resize: 'vertical', minHeight: '60px', width: '95%' }} placeholder="Wpisz diagnozę, podane leki lub zalecenia dla właściciela..." value={leczenie.opis} onChange={e => setLeczenie({...leczenie, opis: e.target.value})} required />

                                <button type="submit" className="submit-btn" style={{ width: '100%', marginTop: '15px' }}>
                                    💾 Zapisz leczenie i zakończ wizytę
                                </button>
                            </form>
                        )}
                    </li>
                ))}
                {oczekujace.length === 0 && <p style={{ color: '#94a3b8' }}>Brak oczekujących pacjentów. ☕</p>}
            </ul>

            <h3 className="subsection-title" style={{ marginTop: '40px' }}>📁 Historia zrealizowanych wizyt ({zakonczenie.length})</h3>
            <ul className="item-list" style={{ opacity: 0.85 }}>
                {zakonczenie.map((w) => (
                    <li key={w.id} className="list-item" style={{ backgroundColor: '#f8fafc', borderColor: '#e2e8f0', flexDirection: 'column', alignItems: 'flex-start' }}>
                        <div style={{ display: 'flex', gap: '10px', marginBottom: '5px' }}>
                            <span className="badge" style={{ backgroundColor: '#64748b' }}>ZAKOŃCZONA</span>
                            <strong>{w.zwierze ? w.zwierze.imie : 'Pacjent'}</strong> (Data: {formatujDate(w.dataczas)})
                        </div>
                        {w.zabiegi && w.zabiegi.length > 0 ? (
                            w.zabiegi.map((z, idx) => (
                                <div key={idx} style={{ fontSize: '13px', color: '#64748b', paddingLeft: '15px', borderLeft: '2px solid #cbd5e1', marginTop: '5px' }}>
                                    <strong>{z.nazwa}</strong>: <em>"{z.wywiad || z.opis || 'Brak uwag'}"</em> — Koszt: <strong>{z.cenaBazowa || z.koszt} zł</strong>
                                </div>
                            ))
                        ) : (
                            <div style={{ fontSize: '13px', color: '#64748b', paddingLeft: '15px', borderLeft: '2px solid #cbd5e1', marginTop: '5px' }}>
                                Zrealizowana bez dodatkowego opisu zabiegu.
                            </div>
                        )}
                    </li>
                ))}
            </ul>
        </div>
    );
}

export default App;