--
-- PostgreSQL database dump
--

-- Dumped from database version 16.6
-- Dumped by pg_dump version 16.6

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
    work character varying(20),
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
-- Name: reports; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.reports (
    official_absence_id integer,
    report_id integer NOT NULL,
    status character varying(20) DEFAULT 'unsubmitted'::character varying
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
    subject_id integer
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
-- Data for Name: attendance_ban_histories; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.attendance_ban_histories (official_absence_id, ban_reason, version, remarks) FROM stdin;
\.


--
-- Data for Name: bereavement_histories; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.bereavement_histories (official_absence_id, deceased_name, relationship, version, remarks) FROM stdin;
363	あ	a	1	
\.


--
-- Data for Name: classifications; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.classifications (id, course) FROM stdin;
1	SE・プログラマ
2	WEBプログラマ
3	IT専門資格取得
4	サイバーセキュリティ
5	AIエンジニア
6	中級プログラマ
7	初級パソコン
8	ゲームプログラマ
9	イラストレータ
10	3Dグラフィック
11	ゲームデザイナー
12	グラフィックデザイン
13	WEBデザイン
14	オフィス一般事務
15	ビジネスパソコン
16	販売ビジネス
17	ホテル・ブライダル
18	医療事務
19	電子カルテ
20	医療マネージメント
21	医療パソコン
22	小児クラーク
23	病棟クラーク
24	調剤薬局
\.


--
-- Data for Name: desired_occupations; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.desired_occupations (student_id, business, occupation) FROM stdin;
40104	未選択	未選択
\.


--
-- Data for Name: exception_dates; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.exception_dates (exception_day, weekday_number) FROM stdin;
2024-12-09	3
2024-12-18	5
\.


--
-- Data for Name: job_search_histories; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.job_search_histories (official_absence_id, work, company_name, address, version, remarks, visit_start_hour, visit_start_minute) FROM stdin;
361	briefing	a	a	1		1	1
362	test	a	a	1		2	2
365	jobInterview	a	a	1	あ	1	1
365	jobInterview	a	a	2	あ	1	1
365	jobInterview	a	a	3	あ	1	1
\.


--
-- Data for Name: lessons; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.lessons (lesson_id, lesson_name, period, day_of_week) FROM stdin;
1	国語	1	月
2	数学	2	月
3	理科	3	月
\.


--
-- Data for Name: official_absence_date_histories; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.official_absence_date_histories (official_absence_id, period, official_absence_date, version, lesson_name) FROM stdin;
361	1	2025-01-01	1	システム開発Ⅱ
361	2	2025-01-01	1	システム開発Ⅱ
362	1	2025-01-02	1	システム開発Ⅱ実習
362	2	2025-01-02	1	システム開発Ⅱ実習
363	1	2025-01-03	1	システム開発Ⅰ実習
363	2	2025-01-03	1	システム開発Ⅰ実習
364	1	2025-01-06	1	情報システム演習
364	2	2025-01-06	1	情報システム演習
364	3	2025-01-06	1	資格対策
364	4	2025-01-06	1	資格対策
364	5	2025-01-06	1	プレゼンテーション
365	1	2025-01-13	1	情報システム演習
365	2	2025-01-13	1	情報システム演習
365	3	2025-01-13	1	資格対策
365	5	2025-01-13	1	プレゼンテーション
365	1	2025-01-13	2	情報システム演習
365	2	2025-01-13	2	情報システム演習
365	3	2025-01-13	2	資格対策
365	5	2025-01-13	2	プレゼンテーション
365	1	2025-01-22	2	システム開発Ⅱ
365	2	2025-01-22	2	システム開発Ⅱ
365	1	2025-01-22	3	システム開発Ⅱ
365	2	2025-01-22	3	システム開発Ⅱ
\.


--
-- Data for Name: official_absences; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.official_absences (official_absence_id, student_id, report_required, status, reason, teacher_check, career_check, career_check_required, classroom, grade, course, student_name, student_email) FROM stdin;
363	40104	f	unaccepted	bereavement	f	\N	f	A	2	SE・プログラマ	YourSurName YourGivenName	40104kk@saisen.ac.jp
364	40104	t	unaccepted	seminar	f	f	t	A	2	SE・プログラマ	YourSurName YourGivenName	40104kk@saisen.ac.jp
365	40104	t	unaccepted	jobSearch	f	f	t	A	2	SE・プログラマ	YourSurName YourGivenName	40104kk@saisen.ac.jp
361	40104	t	unaccepted	jobSearch	f	f	t	A	2	SE・プログラマ	YourSurName YourGivenName	40104kk@saisen.ac.jp
362	40104	t	unaccepted	jobSearch	f	f	t	A	2	SE・プログラマ	YourSurName YourGivenName	40104kk@saisen.ac.jp
\.


--
-- Data for Name: other_histories; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.other_histories (official_absence_id, other_reason, version, remarks) FROM stdin;
\.


--
-- Data for Name: reports; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.reports (official_absence_id, report_id, status) FROM stdin;
361	98	unsubmitted
362	99	unsubmitted
363	100	unnecessary
364	101	unsubmitted
365	102	unsubmitted
\.


--
-- Data for Name: seminar_histories; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.seminar_histories (official_absence_id, seminar_name, location, venue_name, version, remarks, visit_start_hour, visit_start_minute) FROM stdin;
364	あ	あ	あ	1		4	4
\.


--
-- Data for Name: students; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.students (student_id, grade, class, course_id, number, name, hope_occupation, hope_industry) FROM stdin;
40104	2	A	1	2	木谷 恒希	情報	SE・PG
\.


--
-- Data for Name: submitted_date_histories; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.submitted_date_histories (official_absence_id, version, submitted_date) FROM stdin;
361	1	2025-01-04
362	1	2025-01-04
363	1	2025-01-04
364	1	2025-01-04
365	1	2025-01-09
365	2	2025-01-09
365	3	2025-01-09
\.


--
-- Data for Name: time_tables; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.time_tables (grade, classroom, course, semester, weekday_number, period, subject_id) FROM stdin;
\.


--
-- Name: classifications_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.classifications_id_seq', 24, true);


--
-- Name: official_absences_official_absence_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.official_absences_official_absence_id_seq', 365, true);


--
-- Name: reports_report_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.reports_report_id_seq', 102, true);


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

