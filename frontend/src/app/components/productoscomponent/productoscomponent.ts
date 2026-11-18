import { ChangeDetectionStrategy, Component } from '@angular/core';

@Component({
  changeDetection: ChangeDetectionStrategy.Eager,
  selector: 'app-productoscomponent',
  standalone: false,
  styleUrl: './productoscomponent.css',
  templateUrl: './productoscomponent.html',
})
export class Productoscomponent {}
