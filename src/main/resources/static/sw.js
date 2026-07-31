const CACHE_NAME = 'typemaster-v1';
const ASSETS_TO_CACHE = [
  '/',
  '/index.html',
  '/typing.html',
  '/result.html',
  '/auth.html',
  '/assets/css/style.min.css',
  '/assets/css/home.min.css',
  '/assets/css/typing.min.css',
  '/assets/css/result.min.css',
  '/assets/css/auth.min.css',
  '/assets/css/blog.min.css',
  '/assets/js/app.min.js',
  '/assets/js/typing.min.js',
  '/assets/js/stats.min.js',
  '/assets/js/timer.min.js',
  '/assets/js/data.min.js',
  '/assets/js/auth.min.js',
  '/assets/images/logo.svg',
  '/assets/images/logo.png',
  '/manifest.json'
];

self.addEventListener('install', (event) => {
  event.waitUntil(
    caches.open(CACHE_NAME).then((cache) => {
      return cache.addAll(ASSETS_TO_CACHE);
    }).then(() => self.skipWaiting())
  );
});

self.addEventListener('activate', (event) => {
  event.waitUntil(
    caches.keys().then((cacheNames) => {
      return Promise.all(
        cacheNames.filter((name) => name !== CACHE_NAME).map((name) => caches.delete(name))
      );
    }).then(() => self.clients.claim())
  );
});

self.addEventListener('fetch', (event) => {
  if (event.request.method !== 'GET') return;
  
  // Exclude API requests from static caching
  if (event.request.url.includes('/api/')) {
    event.respondWith(
      fetch(event.request).catch(() => caches.match(event.request))
    );
    return;
  }

  event.respondWith(
    caches.match(event.request).then((cachedResponse) => {
      if (cachedResponse) {
        // Return cached asset and update cache in background (stale-while-revalidate)
        fetch(event.request).then((networkResponse) => {
          if (networkResponse && networkResponse.status === 200) {
            caches.open(CACHE_NAME).then((cache) => cache.put(event.request, networkResponse));
          }
        }).catch(() => {});
        return cachedResponse;
      }
      return fetch(event.request).then((networkResponse) => {
        if (networkResponse && networkResponse.status === 200 && event.request.url.startsWith(self.location.origin)) {
          const responseToCache = networkResponse.clone();
          caches.open(CACHE_NAME).then((cache) => cache.put(event.request, responseToCache));
        }
        return networkResponse;
      });
    })
  );
});
