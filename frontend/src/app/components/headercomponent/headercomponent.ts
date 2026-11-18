import { ChangeDetectionStrategy, Component } from '@angular/core';

@Component({
  changeDetection: ChangeDetectionStrategy.Eager,
  selector: 'app-headercomponent',
  standalone: false,
  styleUrl: './headercomponent.css',
  templateUrl: './headercomponent.html',
})
export class Headercomponent {}
