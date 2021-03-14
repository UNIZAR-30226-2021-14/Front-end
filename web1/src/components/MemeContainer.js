import React from 'react'
import Meme from './Meme'

import Search from './Search'
import { getMemesByPopular, getMemesBySearch} from '../services/memes'
//Contendrá subcomponentes
// El único método obligatorio a usar al heredar de component es render, debe devolver un elemento válido: cadena, rull, elemento de react (como h1)...
class MemeContainer extends React.Component {
    constructor (props) {
        super(props)

        this.state = { //state siempre es un objeto
            //name:'name from state'
            meme: [], //array vacío
            isFectch: true //Para saber si está cargando o no
        }
    }
    // Se ejecuta una vez al acabar de montarse el componente
    async componentDidMount (){
        //alert('montado!')
        //this.setState({name: 'name from componentDidMount'})
        /*fetch('https://api.imgflip.com/get_memes')
            .then(response => response.json())
            .then(memesJason => this.setState({meme: memesJason.data.memes,isFectch:false}))
        */
        const responseJson = await getMemesByPopular()
        console.log(responseJson)
        this.setState({meme: responseJson.data.memes, isFectch : false})
    }
    handleSearch = async (search) => { //e es un objeto que corresponde a un evento
      console.log(search) //Nos podemos comunicar con nuestro hijo Search
      // await getMemesBySearch(search)
      //console.log(responseJson)
      //this.setState({meme: responseJson.data.memes}) 
    }

    render () {
        //const name = this.state.name
        //console.log(this.props)
        //this.setState({c: this.state.c++}) No hacerlo aquí pues se ejecuta de forma infinita
        //return <Meme name={name}/>
        //{} Para objetos
        const { isFectch,meme } = this.state //Esto sirve para indicar que ambos se extraen de this.state, así que no haría falta indicarlo al llamarlos (destructuración). Es como hacer const meme = this.state.meme
       /* if (isFectch){
            return "Loading..."
        }*/
        /*const nombre = this.state.meme[1].name
        return <Meme name={nombre}/>*/
        return (
          //para indicar conjunto de elementos, <> equivale a <React.Fragment> 
          <React.Fragment> 
            <Search handleSearch={this.handleSearch}/>
              
              {
                isFectch && 'Loading...' //Si es true entonces muestra loading... Si loading fuera componente se pondría <Loading/> 
              }
              
              {
                (!isFectch && !meme.length) && 'No hay memes que mostrar' //Si ha acabado de cargar y no hay memes muestra no hay mmemes
              }
              
              
              <section className="meme-container">
                {
                  meme.map((meme) => <Meme 
                    imageUrl = {meme.url} 
                    name={meme.name} 
                    box_count={meme.box_count}
                    key={meme.id}
                  />) //...meme coge las propiedades del objeto meme
                }
            </section>
          </React.Fragment>
          
            // meme.map((meme) => <Meme name={meme.name} key={meme.id}/>) //key para identificar cada elemento meme de la lisat
        )
    }
}
export default MemeContainer