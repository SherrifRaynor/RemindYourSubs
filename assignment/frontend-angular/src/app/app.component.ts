import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  // Global toast state could be managed here or in a service
  // For now, only using local state in HTML
  toast = {
    show: false,
    message: '',
    type: 'success' as 'success' | 'error'
  };
}
