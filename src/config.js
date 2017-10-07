let env = process.env.NODE_ENV || 'development';
let env_config = require('./config.'+env+'.json');

let baseUrl = env_config.apiHost + env_config.apiPath;


const config = {
	baseUrl: baseUrl,
	loginEndpoint: baseUrl + env_config.loginEndpoint,
	profileEndpoint: baseUrl + env_config.profileEndpoint,
	uploadImageEndpoint: baseUrl + env_config.uploadImageEndpoint,
};

export {config};
