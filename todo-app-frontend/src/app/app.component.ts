import { Component } from '@angular/core';
import { ConfirmationService } from "primeng/api";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
  providers: [ConfirmationService]
})
export class AppComponent {
  title = 'todo-app-frontend';
}
