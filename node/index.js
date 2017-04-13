const express = require('express');
const directory = '/tmp/';
const uuid = require('uuid');
const fs = require('fs');
const formidable = require('express-formidable');
const cluster = require('express-cluster');
const numCPUs = require('os').cpus().length;
const Busboy = require('busboy');
const path = require('path');

cluster(function(worker) {
  const app = express();

  const fpath = (id) => `${directory}upload_${id}`;
  const deleteFile = (id) => new Promise(res => fs.unlink(fpath(id), () => res()));

  app.get('/:id', (req, res) => {
    const rs = fs.createReadStream(fpath(req.params.id));
    rs.on('error', (err) => console.log(err) || res.status(400).end());
    rs.pipe(res);
    return rs.on('end', () => res.end());
  });

  app.post('/', (req, res) => {
    const busboy = new Busboy({ headers: req.headers });
    const id = uuid.v4();

    busboy.on('file', function(fieldname, file, filename, encoding, mimetype) {
      file.pipe(fs.createWriteStream(fpath(id)));
    });

    busboy.on('finish', function() {
      res.send(id);
    });
    return req.pipe(busboy);
  });

  app.delete('/:id', (req, res) => deleteFile(req.params.id).then(() => res.send('ok')));

  app.put('/:id', (req, res) => {
    const { params } = req;
    const { id } = params;
    const busboy = new Busboy({ headers: req.headers });
    const newID = uuid.v4();

    busboy.on('file', (fieldname, file, filename, encoding, mimetype) => {
      file.pipe(fs.createWriteStream(fpath(newID)));
    });

    busboy.on('finish', () => {
      fs.rename(fpath(newID), fpath(id), () => res.send('ok'));
    });  
    return req.pipe(busboy);
  });

  return app.listen(3000);
}, {count: numCPUs})


