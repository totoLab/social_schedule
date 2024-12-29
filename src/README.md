# Social Media Content Scheduler

A Java application that helps manage and schedule social media content across a team, with support for different content types, weekly schedules, and visual calendar generation.

## Features

- Automated content scheduling with fair distribution among team members
- Support for multiple content types with different weights
- Configurable weekly schedules that can alternate
- Visual calendar generation with color-coded assignments
- JSON-based configuration and schedule storage
- Fair workload distribution based on content weights
- Calendar visualization with customizable styling

## Dependencies (Maven)

- Jackson library for JSON processing
- Java AWT and ImageIO for calendar generation

## Configuration

The system uses a JSON configuration file (`config.example.json`) that includes:
- Team member information and their assigned colors
- Weekly schedule patterns
- First weekday preference
- Visual formatting settings

## Usage
Can be found as example in `Main.java` class.

## Implementation Details

### Scheduling Algorithm
- Distributes content based on content type weights
- Ensures fair distribution among team members
- Follows configurable weekly patterns
- Maintains balanced workload across the team

### Calendar Generation
- Creates PNG images for each month
- Color-codes content by team member
- Includes content type and assignment information
- Supports custom fonts and styling

## Output

The system generates two types of output:
1. JSON schedule file containing all assignments
2. Visual calendar images in PNG format for each month

Calendar images are saved in the `schedule_images/` directory with the naming pattern: `{month}_{year}_calendar.png`