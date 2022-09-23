import _ from 'lodash'

export function filter (pathName, routeMap) {
  const root = { name: null, children: routeMap }
  const node = dfs(pathName, root)
  return node
}

function dfs (pathName, node) {
  if (node.name === pathName) {
    return node
  }
  if (!_.isArray(node.children)) {
    return null
  }
  for (let index = 0; index < node.children.length; index++) {
    const v = node.children[index]
    const u = dfs(pathName, v)
    if (u !== null) {
      return u
    }
  }
  return null
}
