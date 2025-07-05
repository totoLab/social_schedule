-- PostgreSQL Schema for Spring Boot Content Scheduling System

-- People Table
CREATE TABLE people (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    color VARCHAR(7), -- Hex color code for UI display
    is_active BOOLEAN DEFAULT TRUE, -- Soft delete flag
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Content Types Table
CREATE TABLE content_types (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL,
    description TEXT, -- Optional description of content type
    is_active BOOLEAN DEFAULT TRUE, -- Soft delete flag
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Schedule Templates Table (for bulk creation only)
CREATE TABLE schedule_templates (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT, -- Optional template description
    is_active BOOLEAN DEFAULT TRUE, -- Soft delete flag
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Template Rules Table (defines template patterns)
CREATE TABLE template_rules (
    id SERIAL PRIMARY KEY,
    template_id INTEGER NOT NULL,
    day_of_week INTEGER NOT NULL, -- 0=Sunday, 1=Monday, ..., 6=Saturday
    content_type_id INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_template_rules_template 
        FOREIGN KEY (template_id) REFERENCES schedule_templates(id) ON DELETE CASCADE,
    CONSTRAINT fk_template_rules_content_type 
        FOREIGN KEY (content_type_id) REFERENCES content_types(id),
    CONSTRAINT chk_day_of_week CHECK (day_of_week >= 0 AND day_of_week <= 6)
);

-- Schedules Table (Actual Schedule Instances)
CREATE TABLE schedules (
    id SERIAL PRIMARY KEY,
    person_id INTEGER NOT NULL,
    content_type_id INTEGER NOT NULL,
    scheduled_date DATE NOT NULL,
    status VARCHAR(50) DEFAULT 'scheduled', -- scheduled, completed, cancelled, rescheduled
    notes TEXT, -- Optional notes for this schedule instance
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_schedules_person 
        FOREIGN KEY (person_id) REFERENCES people(id),
    CONSTRAINT fk_schedules_content_type 
        FOREIGN KEY (content_type_id) REFERENCES content_types(id),
    CONSTRAINT chk_status CHECK (status IN ('scheduled', 'completed', 'cancelled', 'rescheduled'))
);

-- Indexes for performance
CREATE INDEX idx_person_date ON schedules(person_id, scheduled_date);
CREATE INDEX idx_scheduled_date ON schedules(scheduled_date);
CREATE INDEX idx_status ON schedules(status);
CREATE INDEX idx_template_rules_template ON template_rules(template_id);
CREATE INDEX idx_template_rules_day ON template_rules(day_of_week);

-- Unique constraint to prevent duplicate template rules
CREATE UNIQUE INDEX idx_template_day_unique ON template_rules(template_id, day_of_week);

-- Triggers for updated_at timestamps
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_people_updated_at BEFORE UPDATE ON people
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_schedule_templates_updated_at BEFORE UPDATE ON schedule_templates
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_template_rules_updated_at BEFORE UPDATE ON template_rules
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_schedules_updated_at BEFORE UPDATE ON schedules
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Sample data for testing
INSERT INTO content_types (name, description) VALUES
    ('Blog Post', 'Written blog content'),
    ('Video', 'Video content creation'),
    ('Social Media', 'Social media posts'),
    ('Newsletter', 'Email newsletter content'),
    ('Podcast', 'Audio podcast content');

INSERT INTO people (name, color) VALUES
    ('John Doe', '#FF5733'),
    ('Jane Smith', '#33A1FF'),
    ('Mike Johnson', '#33FF57');

INSERT INTO schedule_templates (name, description) VALUES
    ('Weekly Content Plan', 'Standard weekly content schedule'),
    ('Blog Heavy Week', 'Focus on blog content with social support');

INSERT INTO template_rules (template_id, day_of_week, content_type_id) VALUES
    (1, 1, 1), -- Monday: Blog Post
    (1, 3, 2), -- Wednesday: Video
    (1, 5, 3), -- Friday: Social Media
    (2, 1, 1), -- Monday: Blog Post
    (2, 2, 1), -- Tuesday: Blog Post
    (2, 4, 3), -- Thursday: Social Media
    (2, 6, 3); -- Saturday: Social Media
