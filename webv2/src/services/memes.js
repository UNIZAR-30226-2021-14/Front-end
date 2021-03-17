//LAMADAS A API
const baseURL = 'https://api.imgflip.com'
export async function getMemesByPopular () {
  const response = await fetch(`${baseURL}/get_memes`)
  const responseJson = await response.json() //await solo para responder promesas
  return responseJson   
}

export async function getMemesBySearch (q){
  const response = await fetch(`${baseURL}/get_memes`)
  const responseJson = await response.json() // me lo da aí en formato json
  return responseJson
}


export default {
  getMemesByPopular
}