import { useEffect, useState } from 'react'

const api = (path = '', opt) =>
    fetch('/api/quotes' + path, opt)
        .then(r => (r.ok ? r.json().catch(() => null) : Promise.reject(r)))


const json = (method, body) => ({ method, headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(body) })
const blank = { text: '', author: '', source: '', imageUrl: '', mine: false }
const tilt = id => ((id * 37) % 7) - 3

export default function App() {
  const [quotes, setQuotes] = useState([])
  const [shelf, setShelf] = useState('all') // all | mine | drafts
  const [form, setForm] = useState(null)
  const [err, setErr] = useState('')

  const load = () => api('?all=true')
                                  .then(setQuotes)
                                  .catch(() => setErr('The backend isn’t answering. Start Spring Boot on port 8080.'))

  useEffect(() => { load() }, [])

  const shown = quotes.filter(q => shelf === 'drafts' ? q.status === 'DRAFT' : q.status === 'PUBLISHED' && (shelf === 'mine' ? q.mine : true))

  const save = async e => {
    e.preventDefault()
    await (form.id ? api('/' + form.id, json('PUT', form)) : api('', json('POST', form)))
    setForm(null); load()
  }
  const toggle = q => api(`/${q.id}/publish?on=${q.status !== 'PUBLISHED'}`, { method: 'PATCH' }).then(load)
  const remove = q => confirm('Tear this page out?') && api('/' + q.id, { method: 'DELETE' }).then(load)

  return (
    <>
      <header className="masthead">
        <h1>Cool Girl Monologue</h1>
        <p>Lines I keep, lines I borrowed, and the poets who said it first.</p>
        <nav>
          {[['all', 'Everything'], ['mine', 'Written by me'], ['drafts', 'Drafts']].map(([k, label]) => (
            <button key={k} className={shelf === k ? 'on' : ''} onClick={() => setShelf(k)}>{label}</button>
          ))}
          <button className="add" onClick={() => setForm(blank)}>Write a quote</button>
        </nav>
      </header>

      {err && <p className="err">{err}</p>}
      {!err && !shown.length && <p className="empty">Nothing on this shelf yet. Write a quote to start it.</p>}

      <main className="wall">
        {shown.map(q => (
          <article key={q.id} className="card" style={{ '--r': tilt(q.id) + 'deg' }}>
            <span className="tape" />
            {q.imageUrl && <img src={q.imageUrl} alt="" onError={e => (e.currentTarget.style.display = 'none')} />}
            <blockquote>{q.text}</blockquote>
            <footer>
              <b>{q.author}</b>{q.source && <i>, {q.source}</i>}
              {q.status === 'DRAFT' && <em className="stamp">draft</em>}
            </footer>
            <div className="tools">
              <button onClick={() => setForm(q)}>Edit</button>
              <button onClick={() => toggle(q)}>{q.status === 'PUBLISHED' ? 'Unpublish' : 'Publish'}</button>
              <button onClick={() => remove(q)}>Delete</button>
            </div>
          </article>
        ))}
      </main>

      {form && (
        <div className="veil" onClick={() => setForm(null)}>
          <form className="sheet" onClick={e => e.stopPropagation()} onSubmit={save}>
            <h2>{form.id ? 'Edit this quote' : 'Write a quote'}</h2>

            <textarea required rows="4" placeholder="The words" value={form.text} onChange={e => setForm({ ...form, text: e.target.value })} />

            <input required placeholder="Who said it" value={form.author} onChange={e => setForm({ ...form, author: e.target.value })} />

            <input placeholder="Book, poem or place (optional)" value={form.source || ''} onChange={e => setForm({ ...form, source: e.target.value })} />

            <input placeholder="Pinterest or image link (optional)" value={form.imageUrl || ''} onChange={e => setForm({ ...form, imageUrl: e.target.value })} />

            <label><input type="checkbox" checked={form.mine} onChange={e => setForm({ ...form, mine: e.target.checked })} /> I wrote this</label>

            <div className="row"><button type="button" onClick={() => setForm(null)}>Cancel</button><button className="add">Save quote</button></div>
          </form>
        </div>
      )}
    </>
  )
}
