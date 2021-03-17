import React from 'react'
import PasswordStrengthMeter from './PasswordStrengthMeter';
import {CopyToClipboard} from 'react-copy-to-clipboard';

class CuerpoSeguridad extends React.Component {

  constructor(props){
    super(props)
    this.state = {
      isFetch: true,
      password:'',
      copied:false
    }
  }

  render () {
    const { isFectch,password,copied} = this.state
    return (
      <>
        {
          isFectch && 'Loading...' //Si es true entonces muestra loading... Si loading fuera componente se pondría <Loading/> 
        }
      <div className="passwdMeter">
          <br/>
          <strong>Introduzca una contraseña a validar</strong>
          <div className="meter">
            <br/>
            <input autoComplete="off" type="password" onChange={e => this.setState({ password: e.target.value,copied:false})} />
            <CopyToClipboard text={password} onCopy={e => this.setState({ copied:true})}><button className="copyTC">Copiar</button></CopyToClipboard>
            {copied ? <span style={{color: 'red'}}>Copiado</span> : null}
            <PasswordStrengthMeter password = {password}/>
          </div>
      </div>
      </>
    )
  }
}

export default CuerpoSeguridad





