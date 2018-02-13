function deleteAjax(url, success, failure) {
    var xhr = new XMLHttpRequest();
    xhr.open('DELETE', url);
    xhr.onreadystatechange = function () {
        if (this.readyState === 4) {
            if (this.status === 200) {
                success();
            } else {
                failure();
            }
        }
    };
    xhr.setRequestHeader('X-Requested-With', 'XMLHttpRequest');
    xhr.send();
    return xhr;
}
