export const CookieUtils = {
  set(name: string, value: string, maxAgeSeconds = 86400): void {
    document.cookie = `${name}=${encodeURIComponent(value)}; path=/; max-age=${maxAgeSeconds}; SameSite=Strict`;
  },

  get(name: string): string | null {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) {
      return decodeURIComponent(parts.pop()!.split(';').shift()!);
    }
    return null;
  },

  remove(name: string): void {
    document.cookie = `${name}=; path=/; max-age=0; SameSite=Strict`;
  },
};
