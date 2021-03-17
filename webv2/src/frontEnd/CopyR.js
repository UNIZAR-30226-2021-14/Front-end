import React from 'react'
import PropTypes from 'prop-types'

const CopyR= ({children}) => <footer><p className="copyR">{children}</p></footer>

CopyR.propTypes = {
    children: PropTypes.string.isRequired
}
export default CopyR

