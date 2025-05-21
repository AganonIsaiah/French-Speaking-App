import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChatInputResponseComponent } from './chat-input-response.component';

describe('ChatInputResponseComponent', () => {
  let component: ChatInputResponseComponent;
  let fixture: ComponentFixture<ChatInputResponseComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ChatInputResponseComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ChatInputResponseComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
