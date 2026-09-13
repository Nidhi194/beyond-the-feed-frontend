import { useEffect, useMemo, useState } from 'react'
import { BrowserRouter, Link, Route, Routes, useParams } from 'react-router-dom'
import 'bootstrap/dist/css/bootstrap.min.css'
import './App.css'

const fallbackCategories = [
  { slug: 'mind', label: 'Instagram & The Mind', short: 'Mind', description: 'Comparison, attention, self-expression and the inspiration we find in a shared visual world.', color: 'coral' },
  { slug: 'creativity', label: 'Creativity & Communities', short: 'Creativity', description: 'Creators, niche communities and the collaborations that make discovery feel human.', color: 'lime' },
  { slug: 'business', label: 'Instagram for Business', short: 'Business', description: 'How small businesses and independent creators turn a feed into a digital storefront.', color: 'blue' },
  { slug: 'habits', label: 'Healthy Digital Habits', short: 'Habits', description: 'Mindful scrolling, intentional use and boundaries for a healthier relationship with the feed.', color: 'yellow' },
]

const previewArticles = [
  { id: 1, slug: 'the-post-post-instagram', title: 'The Post-Post Instagram', excerpt: 'How Instagram posting culture is moving from polished performance toward smaller, more honest audiences.', body: 'The pressure to make every Instagram post look like proof of a perfect life has softened. Photo dumps, quieter captions and Close Friends stories make room for the unfinished, the ordinary and the people who already know the context. The feed is still a stage, and comparison has not disappeared, but posting can now feel less like a performance review and more like choosing what is worth sharing.', category: 'Instagram & The Mind', categorySlug: 'mind', author: 'Maya Chen', readTime: '6 min read', image: 'https://images.unsplash.com/photo-1611162617474-5b21e879e113?auto=format&fit=crop&w=1200&q=85', featured: true, source: 'Instagram', sourceUrl: 'https://about.instagram.com/blog/announcements/instagram-ranking-explained', publishedAt: '2023-05-31' },
  { id: 2, slug: 'your-niche-is-already-here', title: 'Your Niche Is Already Here', excerpt: 'Reels, carousels and Instagram communities are turning small interests into places to learn, make and belong.', body: 'Instagram discovery can begin with a Reel, continue through a carousel and end in a page devoted to an interest you did not know had a community. That path can become a rabbit hole, but it can also introduce a beginner to a creative practice, a collaborator or a language for something they already love. The useful question is whether the algorithm keeps us watching or helps us take part.', category: 'Creativity & Communities', categorySlug: 'creativity', author: 'Ari Okafor', readTime: '8 min read', image: 'https://images.unsplash.com/photo-1492684223066-81342ee5ff30?auto=format&fit=crop&w=900&q=85', source: 'Instagram', sourceUrl: 'https://about.instagram.com/blog/announcements/instagram-ranking-explained', publishedAt: '2023-05-31' },
  { id: 3, slug: 'the-link-in-bio-is-a-business-model', title: 'The Link in Bio Is a Business Model', excerpt: 'How Instagram helps creators and small businesses turn discovery, collaborations and visual storytelling into a storefront.', body: 'For a small business or independent creator, Instagram can be a shop window, portfolio and first conversation in one place. A Reel can introduce the process, a carousel can answer a customer question and a collaboration can bring two communities together. The platform does not remove the work of building a sustainable business, but it can make a point of view discoverable before a brand has the budget for a physical storefront.', category: 'Instagram for Business', categorySlug: 'business', author: 'Lena Ortiz', readTime: '7 min read', image: 'https://images.unsplash.com/photo-1556742049-0cfed4f6a45d?auto=format&fit=crop&w=900&q=85', source: 'Instagram', sourceUrl: 'https://about.instagram.com/blog/announcements/instagram-ranking-explained', publishedAt: '2023-05-31' },
  { id: 4, slug: 'a-feed-you-can-live-with', title: 'A Feed You Can Live With', excerpt: 'How intentional scrolling, private sharing and Instagram’s own safety controls can lower the pressure to be always on.', body: 'A healthier Instagram practice starts with deciding what the app is for. Follow accounts that teach or delight you, use private sharing when a moment belongs to a smaller circle, and take a break when the feed turns comparison into background noise. Instagram has introduced controls intended to make experiences more age-appropriate and manageable, but the most useful boundary is still the one that leaves enough attention for life away from the screen.', category: 'Healthy Digital Habits', categorySlug: 'habits', author: 'Priya Shah', readTime: '6 min read', image: 'https://images.unsplash.com/photo-1499209974431-9dddcece7f88?auto=format&fit=crop&w=900&q=85', source: 'Meta Newsroom', sourceUrl: 'https://about.fb.com/news/2024/01/teen-protections-age-appropriate-experiences-on-our-apps/', publishedAt: '2024-01-09' },
]

function App() {
  const [articles, setArticles] = useState([])
  const [categories, setCategories] = useState(fallbackCategories)
  const [query, setQuery] = useState('')
  const [menuOpen, setMenuOpen] = useState(false)

  useEffect(() => {
    fetch('/api/articles')
      .then((response) => response.ok ? response.json() : Promise.reject(new Error('API unavailable')))
      .then((data) => setArticles(Array.isArray(data) ? data : data.articles || []))
      .catch(() => setArticles(previewArticles))
  }, [])

  useEffect(() => {
    fetch('/api/categories')
      .then((response) => response.ok ? response.json() : Promise.reject(new Error('API unavailable')))
      .then((data) => setCategories(data.map((category, index) => ({
        ...category,
        label: category.name,
        short: fallbackCategories.find((fallback) => fallback.slug === category.slug)?.short || category.name,
        color: fallbackCategories.find((fallback) => fallback.slug === category.slug)?.color || fallbackCategories[index % fallbackCategories.length].color,
      }))))
      .catch(() => setCategories(fallbackCategories))
  }, [])

  const filteredArticles = useMemo(() => articles.filter((article) => {
    const haystack = `${article.title} ${article.excerpt} ${article.category}`.toLowerCase()
    return haystack.includes(query.toLowerCase())
  }), [articles, query])

  return (
    <BrowserRouter>
      <header className="site-header">
        <Link to="/" className="wordmark">Beyond <span>the</span> Feed</Link>
        <button className="menu-button d-lg-none" onClick={() => setMenuOpen(!menuOpen)} aria-label="Toggle navigation">☰</button>
        <nav className={`main-nav ${menuOpen ? 'is-open' : ''}`}>
          <Link to="/category/mind" onClick={() => setMenuOpen(false)}>The Mind</Link>
          <Link to="/category/creativity" onClick={() => setMenuOpen(false)}>Creativity</Link>
          <Link to="/category/business" onClick={() => setMenuOpen(false)}>Business</Link>
          <Link to="/category/habits" onClick={() => setMenuOpen(false)}>Digital Habits</Link>
          <Link to="/about" onClick={() => setMenuOpen(false)}>About</Link>
        </nav>
        <label className="search-box"><span>⌕</span><input value={query} onChange={(event) => setQuery(event.target.value)} placeholder="Search stories" aria-label="Search stories" /></label>
      </header>
      <main>
        <Routes>
          <Route path="/" element={<Home articles={filteredArticles} query={query} categories={categories} />} />
          <Route path="/category/:slug" element={<CategoryPage articles={filteredArticles} categories={categories} />} />
          <Route path="/article/:slug" element={<ArticlePage articles={articles.length ? articles : previewArticles} />} />
          <Route path="/about" element={<About />} />
          <Route path="/contact" element={<Contact />} />
          <Route path="/login" element={<Login />} />
        </Routes>
      </main>
      <Footer />
    </BrowserRouter>
  )
}

function Home({ articles, query, categories }) {
  const featured = articles.find((article) => article.featured) || articles[0]
  return <>
    <section className="hero-section container-fluid">
      <div className="hero-copy"><p className="eyebrow">A magazine about Instagram and modern life</p><h1>What the feed<br /><em>gives</em> us.</h1><p className="hero-intro">Stories about Instagram’s pressure and possibility: the attention it captures, the creativity it unlocks, and the communities it helps us find.</p><Link className="arrow-link" to="/category/mind">Explore the issue</Link></div>
      <div className="hero-art"><div className="art-circle"></div><div className="art-caption">Issue 04<br /><strong>Attention</strong><br />and the self</div><img src="https://images.unsplash.com/photo-1497250681960-ef046c08a56e?auto=format&fit=crop&w=1000&q=85" alt="Green leaves in warm light" /></div>
    </section>
    <section className="ticker"><span>Now reading</span><div>Comparison <b>✳</b> Creativity <b>✳</b> Community <b>✳</b> Commerce <b>✳</b> Attention <b>✳</b> Connection</div></section>
    <section className="container editorial-section">
      <div className="section-heading"><div><p className="eyebrow">The latest thinking</p><h2>{query ? `Search results for “${query}”` : 'Stories worth your attention'}</h2></div><Link className="text-link" to="/category/mind">View all stories</Link></div>
      {featured && <Link to={`/article/${featured.slug}`} className="featured-story"><img src={featured.image} alt="" /><div className="featured-copy"><p className="category-label">{featured.category}</p><h3>{featured.title}</h3><p>{featured.excerpt}</p><span className="byline">{featured.author} &nbsp;·&nbsp; {featured.readTime}</span></div></Link>}
      <div className="article-grid">{articles.filter((article) => article.id !== featured?.id).slice(0, 3).map((article) => <ArticleCard key={article.id} article={article} />)}</div>
    </section>
    <CategoryStrip categories={categories} />
  </>
}

function ArticleCard({ article }) { return <Link to={`/article/${article.slug}`} className="article-card"><img src={article.image} alt="" /><p className="category-label">{article.category}</p><h3>{article.title}</h3><p>{article.excerpt}</p><span className="byline">{article.author} &nbsp;·&nbsp; {article.readTime}</span></Link> }

function CategoryPage({ articles, categories }) { const { slug } = useParams(); const category = categories.find((item) => item.slug === slug) || categories[0]; const items = articles.filter((article) => article.categorySlug === slug); return <section className="container category-page"><p className="eyebrow">The collection</p><h1>{category.label}</h1><p className="category-intro">{category.description}</p><div className="article-grid category-grid">{(items.length ? items : articles).map((article) => <ArticleCard key={article.id} article={article} />)}</div></section> }

function ArticlePage({ articles }) {
  const { slug } = useParams()
  const article = articles.find((item) => item.slug === slug) || articles[0]
  const body = article?.body || article?.excerpt || ''
  const [comment, setComment] = useState('')
  const [comments, setComments] = useState([])
  const [commentError, setCommentError] = useState('')
  const [isPosting, setIsPosting] = useState(false)

  useEffect(() => {
    if (!article) return
    fetch(`/api/comments?articleId=${article.id}`)
      .then((response) => response.ok ? response.json() : Promise.reject(new Error('Comments unavailable')))
      .then(setComments)
      .catch(() => setComments([]))
  }, [article])

  if (!article) return <div className="container empty-state">No stories found.</div>

  const submitComment = async (event) => {
    event.preventDefault()
    setCommentError('')
    setIsPosting(true)
    const body = comment.trim()
    try {
      const response = await fetch('/api/comments', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ articleId: Number(article.id), body }),
      })
      if (!response.ok) throw new Error('comment submission failed')
      setComments((current) => [{ body, author: 'Reader' }, ...current])
      setComment('')
    } catch {
      setCommentError('Your comment could not be posted. Please try again.')
    } finally {
      setIsPosting(false)
    }
  }

  return <article className="article-page container">
    <Link className="back-link" to="/">Back to stories</Link>
    <p className="category-label">{article.category}</p>
    <h1>{article.title}</h1>
    <p className="article-dek">{article.excerpt}</p>
    <p className="byline">By {article.author} &nbsp;·&nbsp; {article.readTime}</p>
    {article.publishedAt && <p className="article-source">Published {new Date(`${article.publishedAt}T00:00:00`).toLocaleDateString('en-US', { month: 'long', year: 'numeric' })} · Source: <a href={article.sourceUrl} target="_blank" rel="noreferrer">{article.source}</a></p>}
    <img className="article-hero" src={article.image} alt="" />
    <div className="article-body">
      {body.split(/\n+/).filter(Boolean).map((paragraph) => <p key={paragraph}>{paragraph}</p>)}
      <blockquote>“Attention is the beginning of devotion.”</blockquote>
      <h2>Join the conversation</h2>
      <form onSubmit={submitComment}>
        <textarea value={comment} onChange={(event) => setComment(event.target.value)} placeholder="Add your perspective..." required />
        <button className="button-dark" type="submit" disabled={isPosting}>{isPosting ? 'Posting...' : 'Post comment'}</button>
        {commentError && <p role="alert">{commentError}</p>}
      </form>
      {comments.length > 0 && <div className="comments"><h3>Reader responses</h3>{comments.map((item, index) => <div className="comment" key={item.id || `${item.body}-${index}`}><p>{item.body}</p><span>{item.author}</span></div>)}</div>}
    </div>
  </article>
}

function CategoryStrip({ categories }) { return <section className="category-strip"><div className="container"><p className="eyebrow">Find your way around</p><div className="category-links">{categories.map((category) => <Link key={category.slug} to={`/category/${category.slug}`} className={category.color}><span>{category.short}</span><b></b></Link>)}</div></div></section> }
function About() { return <section className="container simple-page"><p className="eyebrow">Our point of view</p><h1>Life is bigger<br /><em>than the grid.</em></h1><p className="large-copy">Beyond the Feed is an independent digital magazine about Instagram’s influence on modern life. We look past the likes and into the questions underneath: who gets seen, what gets made, and how do we want to be together?</p><Link className="arrow-link" to="/contact">Come say hello</Link></section> }
function Contact() { return <section className="container simple-page contact-page"><p className="eyebrow">Get in touch</p><h1>We’re listening.</h1><form className="contact-form"><input placeholder="Your name" /><input type="email" placeholder="Email address" /><textarea placeholder="What’s on your mind?" rows="5"></textarea><button className="button-dark" type="submit">Send message</button></form></section> }
function Login() { return <section className="container simple-page login-page"><p className="eyebrow">Welcome back</p><h1>Keep reading.</h1><form className="contact-form"><input type="email" placeholder="Email address" /><input type="password" placeholder="Password" /><button className="button-dark" type="submit">Log in</button><p>New here? <Link to="/contact">Create an account</Link></p></form></section> }
function Footer() { return <footer><div className="container footer-inner"><Link to="/" className="wordmark">Beyond <span>the</span> Feed</Link><p>Stories for a life beyond the scroll.</p><div><Link to="/contact">Contact</Link><a href="https://instagram.com" target="_blank" rel="noreferrer">Instagram</a></div></div></footer> }

export default App
