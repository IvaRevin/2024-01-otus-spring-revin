import { Book } from "../models/book";

export interface CommentDTO {
  id?: number;
  text?: string;
  book?: Book;
}
