package az.training.taskmanagement.dto;

import az.training.taskmanagement.model.Priority;
import az.training.taskmanagement.model.TaskStatus;

//biz Patch metodundan ferqli olaraq bir dene de Put ucun dto yaradiriq cunki patchde fieldler required deyil cunki bezi fieldler update olur
//amma Put methodunda full table update olacaq deye butun fieldler required olmalidir. Buna gore update ve patch ucun dtolar ayri yazilir.
public record ReplaceTaskRequest(String title, String description, TaskStatus status, Priority priority) {
}
