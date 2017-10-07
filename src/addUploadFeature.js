import { config } from './config';
import APIUtils from  './apiUtils';
import { fetchUtils } from 'admin-on-rest';

const convertFileToBase64 = file => new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.readAsDataURL(file.rawFile);

    reader.onload = () => resolve(reader.result);
    reader.onerror = reject;
});


const detectPicturesAndUpload = function(properties, params, requestHandler, type, resource) {
    let promises = [];
    for (let prop of properties) {
        if (params.data[prop] && params.data[prop].length) {
            const pictures = params.data[prop].filter(p => p.rawFile instanceof File);
            const alreadUploadedPics = params.data[prop].filter(p => !p.rawFile);
            const url = config.uploadImageEndpoint;
            //the params contain the image as a fileInstance
            params.data[prop] = [];

            for (let picture of pictures) {
                let form = new FormData();
                form.append('file', picture.rawFile);
                form.append('property', prop); // the endpoint needs to return the prop name
                let options = APIUtils.createOptionsForImageUpload(form);
                let promise = fetchUtils.fetchJson(url, options);
                promises.push(promise);
            }
            for (let uploadedPic of alreadUploadedPics) {
                if (!Array.isArray(params.data[prop])) {
                    params.data[prop] = [];
                }
                params.data[prop].push(parseInt(uploadedPic.id));
            }
        }
    }
    return Promise.all(promises).then(results => {
        for (let res of results) {
            let propName = res.json.content[0].property; // so it can be used here
            if (!Array.isArray(params.data[propName])) {
                params.data[propName] = [];
            }
            params.data[propName].push(parseInt(res.json.content[0].id));
        }
        return requestHandler(type, resource, params);
    });
}


const addUploadCapabilities = requestHandler => (type, resource, params) => {
    if ( (type === 'CREATE' || type === "UPDATE")) {
        return detectPicturesAndUpload(["fileA", "fileB"], params, requestHandler, type, resource);
    }

    return requestHandler(type, resource, params);
};

export default addUploadCapabilities;
