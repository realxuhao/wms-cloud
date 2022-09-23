import Cookies from 'js-cookie'

const cookieDomainName = process.env.VUE_APP_COOKIE_DOMAIN_NAME

const accessToken = 'AuthenticationToken'

const getToken = () => {
  const token = Cookies.get(accessToken)
  return token
}

const setToken = (token) => {
  if (cookieDomainName) {
    Cookies.set(accessToken, token, { path: '/', domain: cookieDomainName })
  } else {
    Cookies.set(accessToken, token)
  }
}

const removeToken = () => {
  if (cookieDomainName) {
    Cookies.remove(accessToken, { path: '/', domain: cookieDomainName })
  } else {
    Cookies.remove(accessToken)
  }
}

export { getToken, setToken, removeToken }
