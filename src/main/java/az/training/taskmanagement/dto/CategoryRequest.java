package az.training.taskmanagement.dto;

import az.training.taskmanagement.service.TaskService;

public record CategoryRequest( Long id,String name, String description) {
}
