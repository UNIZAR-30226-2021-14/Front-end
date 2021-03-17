import React from 'react'
import PropTypes from 'prop-types'

const PasswordRevealer = () => {
	const [shown, setShown] = React.useState(false);
  const [password, setPassword] = React.useState('');
  
  const switchShown = () => setShown(!shown);
  const onChange = ({ currentTarget }) => setPassword(currentTarget.value);
  
  return (
    <>
    
  	<label className="password" htmlFor="password__input">Contraseña</label>
      <input
        className="password__input"
        id="password__input"
        onChange={onChange}
        placeholder=" "
        type={shown ? 'text' : 'password'}
        value={password}
      />
      <button className="password__button" onClick={switchShown}>
        {shown ? 'Ocultar' : 'Mostrar'}
      </button>
    
    </>
  );
}

PasswordRevealer.propTypes = {
  change:PropTypes.func.isRequired,
  
}
export default PasswordRevealer
