import { Component, OnInit } from '@angular/core';
import { MatButton } from "@angular/material/button";
import { Book } from "../../models/book";
import { ActivatedRoute, Params, Router } from "@angular/router";
import { MatTableModule } from "@angular/material/table";
import { BookService } from "../../services/book.service";
import { CommentService } from "../../services/comments.service";
import { CommentDTO } from "../../models/comment";

@Component({
  selector: 'app-comment-list',
  standalone: true,
  imports: [MatTableModule, MatButton],
  templateUrl: './comment-list.component.html',
  styleUrl: './comment-list.component.scss'
})
export class CommentListComponent implements OnInit {
  book: Book;
  comments: CommentDTO[] = [];
  displayedColumns: string[] = ['id', 'text', 'actions'];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private commentService: CommentService,
    private bookService: BookService
  ) {
    this.book = {};
  }

  public goToEditComment(comment: CommentDTO) {
    if (!comment || !comment.id) {
      return;
    }
    this.router.navigate(['comment-edit', comment.id]);
  }

  public goToCreateComment() {
    this.router.navigate(['comment-edit']);
  }

  goToBookList() {
    this.router.navigate(['book-list'])
  }

  public deleteComment(comment: CommentDTO) {
    if (!comment || !comment.id) {
      return;
    }
    this.commentService.deleteComment(comment.id).subscribe(() => {
      this.refreshComments();
    });
  }

  private refreshComments() {
    if (!!this.book) {
      let id = this.book.id;
      if (!id) {
        return;
      }
      this.commentService.getCommentsForBook(id).subscribe(data => {
        this.comments = data;
      });
    }
  }

  ngOnInit(): void {
    this.route.params.subscribe((params: Params) => {
      let id = params['id'];
      this.bookService.getBook(id).subscribe(data => {
        this.book = data;
        this.refreshComments();
      });
    });
  }
}
