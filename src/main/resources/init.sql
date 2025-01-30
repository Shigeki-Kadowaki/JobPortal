--
-- PostgreSQL database dump
--

-- Dumped from database version 16.4
-- Dumped by pg_dump version 16.4

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: reason; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.reason AS ENUM (
    'jobSearch',
    'seminar',
    'bereavement',
    'attendanceBan',
    'other'
);


ALTER TYPE public.reason OWNER TO postgres;

--
-- Name: status; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.status AS ENUM (
    'acceptance',
    'unaccepted',
    'rejection',
    'unsubmitted'
);


ALTER TYPE public.status OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: approved_leave_requests; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.approved_leave_requests (
    official_absence_id integer NOT NULL,
    student_id integer,
    official_absence_date date,
    period integer,
    reflected_flag boolean
);


ALTER TABLE public.approved_leave_requests OWNER TO postgres;

--
-- Name: attendance_ban_histories; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.attendance_ban_histories (
    official_absence_id integer,
    ban_reason character varying(64),
    version integer,
    remarks text
);


ALTER TABLE public.attendance_ban_histories OWNER TO postgres;

--
-- Name: bereavement_histories; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.bereavement_histories (
    official_absence_id integer,
    deceased_name character varying(60),
    relationship character varying(30),
    version integer,
    remarks text
);


ALTER TABLE public.bereavement_histories OWNER TO postgres;

--
-- Name: classifications; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.classifications (
    id integer NOT NULL,
    course character varying(40)
);


ALTER TABLE public.classifications OWNER TO postgres;

--
-- Name: classifications_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.classifications_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.classifications_id_seq OWNER TO postgres;

--
-- Name: classifications_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.classifications_id_seq OWNED BY public.classifications.id;


--
-- Name: desired_occupations; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.desired_occupations (
    student_id integer NOT NULL,
    business character varying(40),
    occupation character varying(40)
);


ALTER TABLE public.desired_occupations OWNER TO postgres;

--
-- Name: exception_dates; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.exception_dates (
    exception_day date,
    weekday_number integer
);


ALTER TABLE public.exception_dates OWNER TO postgres;

--
-- Name: job_search_histories; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.job_search_histories (
    official_absence_id integer,
    work character varying(40),
    company_name character varying(60),
    address character varying(150),
    version integer,
    remarks text,
    visit_start_hour integer,
    visit_start_minute integer
);


ALTER TABLE public.job_search_histories OWNER TO postgres;

--
-- Name: lessons; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.lessons (
    lesson_id integer NOT NULL,
    lesson_name character varying(30),
    period integer,
    day_of_week character varying(2)
);


ALTER TABLE public.lessons OWNER TO postgres;

--
-- Name: official_absence_date_histories; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.official_absence_date_histories (
    official_absence_id integer,
    period integer,
    official_absence_date date,
    version integer,
    lesson_name character varying(40)
);


ALTER TABLE public.official_absence_date_histories OWNER TO postgres;

--
-- Name: official_absences; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.official_absences (
    official_absence_id integer NOT NULL,
    student_id integer,
    report_required boolean,
    status character varying(20),
    reason character varying(20),
    teacher_check boolean,
    career_check boolean,
    career_check_required boolean,
    classroom character(1),
    grade integer,
    course character varying(30),
    student_name character varying(30),
    student_email character varying(30)
);


ALTER TABLE public.official_absences OWNER TO postgres;

--
-- Name: official_absences_official_absence_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.official_absences_official_absence_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.official_absences_official_absence_id_seq OWNER TO postgres;

--
-- Name: official_absences_official_absence_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.official_absences_official_absence_id_seq OWNED BY public.official_absences.official_absence_id;


--
-- Name: other_histories; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.other_histories (
    official_absence_id integer,
    other_reason character varying(128),
    version integer,
    remarks text
);


ALTER TABLE public.other_histories OWNER TO postgres;

--
-- Name: report_briefing_histories; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.report_briefing_histories (
    report_id integer NOT NULL,
    version integer NOT NULL,
    briefing_content text,
    impressions text
);


ALTER TABLE public.report_briefing_histories OWNER TO postgres;

--
-- Name: report_exam_histories; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.report_exam_histories (
    report_id integer NOT NULL,
    version integer NOT NULL,
    general_knowledge_type character varying(64),
    national_language_number integer,
    national_language_type character varying(64),
    math_number integer,
    math_type character varying(64),
    english_number integer,
    english_type character varying(64),
    current_affairs_number integer,
    current_affairs_type character varying(64),
    writing_timer integer,
    writing_number_of_characters integer,
    writing_theme character varying(64),
    expertise_number integer,
    expertise_type character varying(64),
    job_question_number integer,
    job_question_type character varying(64),
    spi_language_system_number integer,
    spi_non_language_system_number integer,
    spi_others_number integer,
    personality_diagnosis_number integer,
    personality_diagnosis_type character varying(64),
    others text,
    impressions text,
    general_knowledge_number integer
);


ALTER TABLE public.report_exam_histories OWNER TO postgres;

--
-- Name: report_histories; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.report_histories (
    report_id integer NOT NULL,
    version integer NOT NULL,
    activity_time integer,
    submitted_date date,
    company_name character varying(64)
);


ALTER TABLE public.report_histories OWNER TO postgres;

--
-- Name: report_informal_ceremony_histories; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.report_informal_ceremony_histories (
    report_id integer NOT NULL,
    version integer NOT NULL,
    impressions text
);


ALTER TABLE public.report_informal_ceremony_histories OWNER TO postgres;

--
-- Name: report_interview_histories; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.report_interview_histories (
    report_id integer NOT NULL,
    version integer NOT NULL,
    interviewer_number integer,
    interview_type character varying(32),
    interview_content text,
    impressions text
);


ALTER TABLE public.report_interview_histories OWNER TO postgres;

--
-- Name: report_job_future_selection; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.report_job_future_selection (
    report_id integer NOT NULL,
    version integer NOT NULL,
    employment_intention character varying(32),
    next_action character varying(32)
);


ALTER TABLE public.report_job_future_selection OWNER TO postgres;

--
-- Name: report_other_histories; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.report_other_histories (
    report_id integer NOT NULL,
    version integer NOT NULL,
    activity_content character varying(32),
    impressions text
);


ALTER TABLE public.report_other_histories OWNER TO postgres;

--
-- Name: report_seminar_histories; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.report_seminar_histories (
    report_id integer NOT NULL,
    company_id integer NOT NULL,
    version integer NOT NULL,
    company_name character varying(32),
    manager character varying(32),
    industry character varying(32),
    impressions text,
    employment_intention character varying(32),
    next_action character varying(32)
);


ALTER TABLE public.report_seminar_histories OWNER TO postgres;

--
-- Name: report_training_histories; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.report_training_histories (
    report_id integer NOT NULL,
    version integer NOT NULL,
    impressions text
);


ALTER TABLE public.report_training_histories OWNER TO postgres;

--
-- Name: reports; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.reports (
    official_absence_id integer NOT NULL,
    report_id integer NOT NULL,
    status character varying(32),
    reason character varying(32)
);


ALTER TABLE public.reports OWNER TO postgres;

--
-- Name: reports_report_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.reports_report_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.reports_report_id_seq OWNER TO postgres;

--
-- Name: reports_report_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.reports_report_id_seq OWNED BY public.reports.report_id;


--
-- Name: seminar_histories; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.seminar_histories (
    official_absence_id integer,
    seminar_name character varying(50),
    location character varying(150),
    venue_name character varying(50),
    version integer,
    remarks text,
    visit_start_hour integer,
    visit_start_minute integer
);


ALTER TABLE public.seminar_histories OWNER TO postgres;

--
-- Name: students; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.students (
    student_id integer NOT NULL,
    grade integer,
    class character(1),
    course_id integer,
    number integer,
    name character varying(60),
    hope_occupation character varying(30),
    hope_industry character varying(30)
);


ALTER TABLE public.students OWNER TO postgres;

--
-- Name: submitted_date_histories; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.submitted_date_histories (
    official_absence_id integer NOT NULL,
    version integer NOT NULL,
    submitted_date date
);


ALTER TABLE public.submitted_date_histories OWNER TO postgres;

--
-- Name: time_tables; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.time_tables (
    grade integer,
    classroom character(1),
    course character varying(30),
    semester character varying(10),
    weekday_number integer,
    period integer,
    subject_id integer,
    year integer
);


ALTER TABLE public.time_tables OWNER TO postgres;

--
-- Name: classifications id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.classifications ALTER COLUMN id SET DEFAULT nextval('public.classifications_id_seq'::regclass);


--
-- Name: official_absences official_absence_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.official_absences ALTER COLUMN official_absence_id SET DEFAULT nextval('public.official_absences_official_absence_id_seq'::regclass);


--
-- Name: reports report_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reports ALTER COLUMN report_id SET DEFAULT nextval('public.reports_report_id_seq'::regclass);


--
-- Data for Name: approved_leave_requests; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.approved_leave_requests (official_absence_id, student_id, official_absence_date, period, reflected_flag) FROM stdin;
\.


--
-- Data for Name: attendance_ban_histories; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.attendance_ban_histories (official_absence_id, ban_reason, version, remarks) FROM stdin;
\.


--
-- Data for Name: bereavement_histories; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.bereavement_histories (official_absence_id, deceased_name, relationship, version, remarks) FROM stdin;
\.


--
-- Data for Name: classifications; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.classifications (id, course) FROM stdin;
26	SE・プログラマ
27	WEBプログラマ
28	IT専門資格取得
29	サイバーセキュリティ
30	AIエンジニア
31	中級プログラマ
32	初級パソコン
33	ゲームプログラマ
34	イラストレータ
35	3Dグラフィック
36	ゲームデザイナー
37	グラフィックデザイン
38	WEBデザイン
39	オフィス一般事務
40	ビジネスパソコン
41	販売ビジネス
42	ホテル・ブライダル
43	医療事務
44	電子カルテ
45	医療マネージメント
46	医療パソコン
47	小児クラーク
48	病棟クラーク
49	調剤薬局
\.


--
-- Data for Name: desired_occupations; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.desired_occupations (student_id, business, occupation) FROM stdin;
\.


--
-- Data for Name: exception_dates; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.exception_dates (exception_day, weekday_number) FROM stdin;
\.


--
-- Data for Name: job_search_histories; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.job_search_histories (official_absence_id, work, company_name, address, version, remarks, visit_start_hour, visit_start_minute) FROM stdin;
\.


--
-- Data for Name: lessons; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.lessons (lesson_id, lesson_name, period, day_of_week) FROM stdin;
\.


--
-- Data for Name: official_absence_date_histories; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.official_absence_date_histories (official_absence_id, period, official_absence_date, version, lesson_name) FROM stdin;
\.


--
-- Data for Name: official_absences; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.official_absences (official_absence_id, student_id, report_required, status, reason, teacher_check, career_check, career_check_required, classroom, grade, course, student_name, student_email) FROM stdin;
\.


--
-- Data for Name: other_histories; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.other_histories (official_absence_id, other_reason, version, remarks) FROM stdin;
\.


--
-- Data for Name: report_briefing_histories; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.report_briefing_histories (report_id, version, briefing_content, impressions) FROM stdin;
\.


--
-- Data for Name: report_exam_histories; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.report_exam_histories (report_id, version, general_knowledge_type, national_language_number, national_language_type, math_number, math_type, english_number, english_type, current_affairs_number, current_affairs_type, writing_timer, writing_number_of_characters, writing_theme, expertise_number, expertise_type, job_question_number, job_question_type, spi_language_system_number, spi_non_language_system_number, spi_others_number, personality_diagnosis_number, personality_diagnosis_type, others, impressions, general_knowledge_number) FROM stdin;
\.


--
-- Data for Name: report_histories; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.report_histories (report_id, version, activity_time, submitted_date, company_name) FROM stdin;
\.


--
-- Data for Name: report_informal_ceremony_histories; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.report_informal_ceremony_histories (report_id, version, impressions) FROM stdin;
\.


--
-- Data for Name: report_interview_histories; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.report_interview_histories (report_id, version, interviewer_number, interview_type, interview_content, impressions) FROM stdin;
\.


--
-- Data for Name: report_job_future_selection; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.report_job_future_selection (report_id, version, employment_intention, next_action) FROM stdin;
\.


--
-- Data for Name: report_other_histories; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.report_other_histories (report_id, version, activity_content, impressions) FROM stdin;
\.


--
-- Data for Name: report_seminar_histories; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.report_seminar_histories (report_id, company_id, version, company_name, manager, industry, impressions, employment_intention, next_action) FROM stdin;
\.


--
-- Data for Name: report_training_histories; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.report_training_histories (report_id, version, impressions) FROM stdin;
\.


--
-- Data for Name: reports; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.reports (official_absence_id, report_id, status, reason) FROM stdin;
\.


--
-- Data for Name: seminar_histories; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.seminar_histories (official_absence_id, seminar_name, location, venue_name, version, remarks, visit_start_hour, visit_start_minute) FROM stdin;
\.


--
-- Data for Name: students; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.students (student_id, grade, class, course_id, number, name, hope_occupation, hope_industry) FROM stdin;
\.


--
-- Data for Name: submitted_date_histories; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.submitted_date_histories (official_absence_id, version, submitted_date) FROM stdin;
\.


--
-- Data for Name: time_tables; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.time_tables (grade, classroom, course, semester, weekday_number, period, subject_id, year) FROM stdin;
\.


--
-- Name: classifications_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.classifications_id_seq', 49, true);


--
-- Name: official_absences_official_absence_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.official_absences_official_absence_id_seq', 30, true);


--
-- Name: reports_report_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.reports_report_id_seq', 30, true);


--
-- Name: classifications classifications_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.classifications
    ADD CONSTRAINT classifications_pkey PRIMARY KEY (id);


--
-- Name: desired_occupations desired_occupations_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.desired_occupations
    ADD CONSTRAINT desired_occupations_pkey PRIMARY KEY (student_id);


--
-- Name: lessons lessons_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.lessons
    ADD CONSTRAINT lessons_pkey PRIMARY KEY (lesson_id);


--
-- Name: official_absences official_absences_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.official_absences
    ADD CONSTRAINT official_absences_pkey PRIMARY KEY (official_absence_id);


--
-- Name: report_briefing_histories report_briefing_session_histories_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.report_briefing_histories
    ADD CONSTRAINT report_briefing_session_histories_pkey PRIMARY KEY (report_id, version);


--
-- Name: report_exam_histories report_exam_histories_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.report_exam_histories
    ADD CONSTRAINT report_exam_histories_pkey PRIMARY KEY (report_id, version);


--
-- Name: report_histories report_histories_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.report_histories
    ADD CONSTRAINT report_histories_pkey PRIMARY KEY (report_id, version);


--
-- Name: report_informal_ceremony_histories report_informal_ceremony_histories_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.report_informal_ceremony_histories
    ADD CONSTRAINT report_informal_ceremony_histories_pkey PRIMARY KEY (report_id, version);


--
-- Name: report_interview_histories report_interview_histories_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.report_interview_histories
    ADD CONSTRAINT report_interview_histories_pkey PRIMARY KEY (report_id, version);


--
-- Name: report_job_future_selection report_job_future_selection_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.report_job_future_selection
    ADD CONSTRAINT report_job_future_selection_pkey PRIMARY KEY (report_id, version);


--
-- Name: report_other_histories report_other_histories_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.report_other_histories
    ADD CONSTRAINT report_other_histories_pkey PRIMARY KEY (report_id, version);


--
-- Name: report_seminar_histories report_seminar_histories_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.report_seminar_histories
    ADD CONSTRAINT report_seminar_histories_pkey PRIMARY KEY (report_id, company_id, version);


--
-- Name: report_training_histories report_training_histories_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.report_training_histories
    ADD CONSTRAINT report_training_histories_pkey PRIMARY KEY (report_id, version);


--
-- Name: reports reports_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reports
    ADD CONSTRAINT reports_pkey PRIMARY KEY (report_id);


--
-- Name: students students_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.students
    ADD CONSTRAINT students_pkey PRIMARY KEY (student_id);


--
-- Name: submitted_date_histories submitted_date_histories_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.submitted_date_histories
    ADD CONSTRAINT submitted_date_histories_pkey PRIMARY KEY (official_absence_id, version);


--
-- Name: attendance_ban_histories attendance_ban_histories_official_absence_id_version_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.attendance_ban_histories
    ADD CONSTRAINT attendance_ban_histories_official_absence_id_version_fkey FOREIGN KEY (official_absence_id, version) REFERENCES public.submitted_date_histories(official_absence_id, version);


--
-- Name: bereavement_histories bereavement_histories_official_absence_id_version_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bereavement_histories
    ADD CONSTRAINT bereavement_histories_official_absence_id_version_fkey FOREIGN KEY (official_absence_id, version) REFERENCES public.submitted_date_histories(official_absence_id, version);


--
-- Name: job_search_histories job_search_histories_official_absence_id_version_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.job_search_histories
    ADD CONSTRAINT job_search_histories_official_absence_id_version_fkey FOREIGN KEY (official_absence_id, version) REFERENCES public.submitted_date_histories(official_absence_id, version);


--
-- Name: official_absence_date_histories official_absence_date_historie_official_absence_id_version_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.official_absence_date_histories
    ADD CONSTRAINT official_absence_date_historie_official_absence_id_version_fkey FOREIGN KEY (official_absence_id, version) REFERENCES public.submitted_date_histories(official_absence_id, version);


--
-- Name: other_histories other_histories_official_absence_id_version_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.other_histories
    ADD CONSTRAINT other_histories_official_absence_id_version_fkey FOREIGN KEY (official_absence_id, version) REFERENCES public.submitted_date_histories(official_absence_id, version);


--
-- Name: report_briefing_histories report_briefing_session_histories_report_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.report_briefing_histories
    ADD CONSTRAINT report_briefing_session_histories_report_id_fkey FOREIGN KEY (report_id) REFERENCES public.reports(report_id);


--
-- Name: report_exam_histories report_exam_histories_report_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.report_exam_histories
    ADD CONSTRAINT report_exam_histories_report_id_fkey FOREIGN KEY (report_id) REFERENCES public.reports(report_id);


--
-- Name: report_histories report_histories_report_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.report_histories
    ADD CONSTRAINT report_histories_report_id_fkey FOREIGN KEY (report_id) REFERENCES public.reports(report_id);


--
-- Name: report_informal_ceremony_histories report_informal_ceremony_histories_report_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.report_informal_ceremony_histories
    ADD CONSTRAINT report_informal_ceremony_histories_report_id_fkey FOREIGN KEY (report_id) REFERENCES public.reports(report_id);


--
-- Name: report_interview_histories report_interview_histories_report_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.report_interview_histories
    ADD CONSTRAINT report_interview_histories_report_id_fkey FOREIGN KEY (report_id) REFERENCES public.reports(report_id);


--
-- Name: report_job_future_selection report_job_future_selection_report_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.report_job_future_selection
    ADD CONSTRAINT report_job_future_selection_report_id_fkey FOREIGN KEY (report_id) REFERENCES public.reports(report_id);


--
-- Name: report_other_histories report_other_histories_report_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.report_other_histories
    ADD CONSTRAINT report_other_histories_report_id_fkey FOREIGN KEY (report_id) REFERENCES public.reports(report_id);


--
-- Name: report_seminar_histories report_seminar_histories_report_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.report_seminar_histories
    ADD CONSTRAINT report_seminar_histories_report_id_fkey FOREIGN KEY (report_id) REFERENCES public.reports(report_id);


--
-- Name: report_training_histories report_training_histories_report_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.report_training_histories
    ADD CONSTRAINT report_training_histories_report_id_fkey FOREIGN KEY (report_id) REFERENCES public.reports(report_id);


--
-- Name: reports reports_official_absence_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reports
    ADD CONSTRAINT reports_official_absence_id_fkey FOREIGN KEY (official_absence_id) REFERENCES public.official_absences(official_absence_id);


--
-- Name: seminar_histories seminar_histories_official_absence_id_version_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.seminar_histories
    ADD CONSTRAINT seminar_histories_official_absence_id_version_fkey FOREIGN KEY (official_absence_id, version) REFERENCES public.submitted_date_histories(official_absence_id, version);


--
-- Name: submitted_date_histories submitted_date_histories_official_absence_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.submitted_date_histories
    ADD CONSTRAINT submitted_date_histories_official_absence_id_fkey FOREIGN KEY (official_absence_id) REFERENCES public.official_absences(official_absence_id);


--
-- PostgreSQL database dump complete
--

