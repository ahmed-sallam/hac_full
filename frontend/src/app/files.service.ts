import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {AppService} from "./app.service";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class FilesService {

  constructor(private http: HttpClient, private appService: AppService) {}

  getFile(path:string):Observable<Blob>{
    return this.http.get(`${this.appService.baseApi}/files/${path}`,{responseType: 'blob'});
  }

  uploadImage(file: File, path: string = 'images'):Observable<any>{
    const formData: FormData = new FormData();
    formData.append('file', file, file.name);
    formData.append('path', path);
    const headers = new HttpHeaders({
      'Access-Control-Allow-Origin': '*'
    });
    return this.http.post(`${this.appService.baseApi}/files/images`, formData, {
        headers,
      reportProgress: true,
      observe: 'events',
        responseType: 'text'
    });
  }
}
