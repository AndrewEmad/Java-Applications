USE [master]
GO
/****** Object:  Database [ST-Comm]    Script Date: 12/21/2016 8:23:58 PM ******/
CREATE DATABASE [ST-Comm]
 CONTAINMENT = NONE

GO
USE [ST-Comm]
ALTER DATABASE [ST-Comm] SET COMPATIBILITY_LEVEL = 110
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [ST-Comm].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [ST-Comm] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [ST-Comm] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [ST-Comm] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [ST-Comm] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [ST-Comm] SET ARITHABORT OFF 
GO
ALTER DATABASE [ST-Comm] SET AUTO_CLOSE OFF 
GO
ALTER DATABASE [ST-Comm] SET AUTO_CREATE_STATISTICS ON 
GO
ALTER DATABASE [ST-Comm] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [ST-Comm] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [ST-Comm] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [ST-Comm] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [ST-Comm] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [ST-Comm] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [ST-Comm] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [ST-Comm] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [ST-Comm] SET  DISABLE_BROKER 
GO
ALTER DATABASE [ST-Comm] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [ST-Comm] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [ST-Comm] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [ST-Comm] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [ST-Comm] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [ST-Comm] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [ST-Comm] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [ST-Comm] SET RECOVERY FULL 
GO
ALTER DATABASE [ST-Comm] SET  MULTI_USER 
GO
ALTER DATABASE [ST-Comm] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [ST-Comm] SET DB_CHAINING OFF 
GO
ALTER DATABASE [ST-Comm] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO
ALTER DATABASE [ST-Comm] SET TARGET_RECOVERY_TIME = 0 SECONDS 
GO
EXEC sys.sp_db_vardecimal_storage_format N'ST-Comm', N'ON'
GO
USE [ST-Comm]
GO
/****** Object:  StoredProcedure [dbo].[adminExists]    Script Date: 12/21/2016 8:23:58 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE proc [dbo].[adminExists](@name nvarchar(300),@email nvarchar(MAX),@output bit out)
AS
BEGIN
SET NOCOUNT ON;
SELECT @output=COUNT(@name) FROM [Admin] WHERE AdminName=@name COLLATE Latin1_General_CS_AS 
IF(@output=0)
SELECT @output=COUNT(@email) FROM [Admin] WHERE [E-mail]=@email COLLATE Latin1_General_CS_AS 
END

GO
/****** Object:  StoredProcedure [dbo].[AuthenticateAdmin]    Script Date: 12/21/2016 8:23:58 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE proc [dbo].[AuthenticateAdmin](
@name nvarchar(300),
@password nvarchar(max),
@output bit out
)
AS
BEGIN

SELECT @output=COUNT(AdminName) FROM [Admin] WHERE AdminName=@name COLLATE Latin1_General_CS_AS  AND [Password]=@password COLLATE Latin1_General_CS_AS 
END

GO
/****** Object:  StoredProcedure [dbo].[AuthenticateStudent]    Script Date: 12/21/2016 8:23:58 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE proc [dbo].[AuthenticateStudent](
@name nvarchar(300),
@password nvarchar(max),
@output bit out
)
AS
BEGIN

SELECT @output=COUNT(StudentName) FROM Student WHERE StudentName=@name COLLATE Latin1_General_CS_AS  AND Password=@password COLLATE Latin1_General_CS_AS 
END

GO
/****** Object:  StoredProcedure [dbo].[AuthenticateTeacher]    Script Date: 12/21/2016 8:23:58 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE proc [dbo].[AuthenticateTeacher](
@name nvarchar(300),
@password nvarchar(max),
@output bit out
)
AS
BEGIN

SELECT @output=COUNT(TeacherName) FROM Teacher WHERE TeacherName=@name COLLATE Latin1_General_CS_AS  AND Password=@password COLLATE Latin1_General_CS_AS AND Verified=1
END

GO
/****** Object:  StoredProcedure [dbo].[fetchGame]    Script Date: 12/21/2016 8:23:58 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE proc [dbo].[fetchGame] (@gameName nvarchar(300))AS
BEGIN
SELECT GameName,[Level],CategoryName,TeacherName,NumberOfQuestions,Confirmed
FROM (Game g inner join Category c on g.CategoryID=c.CategoryID) inner join
Teacher t on g.TeacherID=t.TeacherID
 Where GameName=@gameName COLLATE Latin1_General_CS_AS 
END

GO
/****** Object:  StoredProcedure [dbo].[fetchGames]    Script Date: 12/21/2016 8:23:58 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE proc [dbo].[fetchGames](@CategoryName AS nvarchar(MAX)) AS
BEGIN
SELECT GameName FROM Game WHERE Confirmed=1 AND CategoryID IN (
SELECT CategoryID FROM Category Where CategoryName = @CategoryName COLLATE Latin1_General_CS_AS )

END

GO
/****** Object:  StoredProcedure [dbo].[fetchPendingGames]    Script Date: 12/21/2016 8:23:58 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
create proc [dbo].[fetchPendingGames] AS
BEGIN
SELECT GameName FROM Game WHERE Confirmed = 0 
END

GO
/****** Object:  StoredProcedure [dbo].[fetchQuestion]    Script Date: 12/21/2016 8:23:58 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE proc [dbo].[fetchQuestion](@gameName nvarchar(max))
AS
BEGIN

declare @ID int
SELECT @ID=GameID FROM Game WHERE GameName=@gameName COLLATE Latin1_General_CS_AS 
SELECT * FROM Question WHERE GameID=@ID

END

GO
/****** Object:  StoredProcedure [dbo].[fetchTeacherRequests]    Script Date: 12/21/2016 8:23:58 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
create proc [dbo].[fetchTeacherRequests]
AS
BEGIN
SELECT TeacherName FROM Teacher Where Verified=0
END

GO
/****** Object:  StoredProcedure [dbo].[getCategories]    Script Date: 12/21/2016 8:23:58 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
create proc [dbo].[getCategories] AS
BEGIN
SELECT CategoryName FROM Category
END

GO
/****** Object:  StoredProcedure [dbo].[isBlacklisted]    Script Date: 12/21/2016 8:23:58 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE proc [dbo].[isBlacklisted](@email nvarchar(MAX),@output bit out)
AS
BEGIN
SET NOCOUNT ON;
SELECT @output=COUNT(Email) FROM Blacklist WHERE Email=@email COLLATE Latin1_General_CS_AS 
END

GO
/****** Object:  StoredProcedure [dbo].[isUnlocked]    Script Date: 12/21/2016 8:23:58 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROC [dbo].[isUnlocked](
@studentName nvarchar(300),
@gameName nvarchar(300),
@output bit out
)
AS
BEGIN
declare @badge nvarchar(MAX),@lvl int;
SELECT @badge=Badge FROM Student WHERE StudentName=@studentName COLLATE Latin1_General_CS_AS ;

IF @badge='Beginner'
	SET @lvl=1
ELSE IF @badge='Mature'
	SET @lvl=2
ELSE IF @badge='Specialist'
	SET @lvl=3
ELSE IF @badge='Expert' 
	SET @lvl=4

SELECT @output=COUNT(StudentName) FROM Student,Game
WHERE StudentName=@studentName ANd GameName=@gameName COLLATE Latin1_General_CS_AS  AND [Level]<=@lvl


END

GO
/****** Object:  StoredProcedure [dbo].[saveGame]    Script Date: 12/21/2016 8:23:58 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE proc [dbo].[saveGame] (
@gameName AS nvarchar(300),
@level int,
@CategoryName nvarchar(max),
@TeacherName nvarchar(300),
@numOfQuestions int,
@output int OUT

) 
AS
BEGIN
SET NOCOUNT ON;
DECLARE @catID int;
set @catID = (SELECT CategoryID FROM Category WHERE CategoryName=@CategoryName)
DECLARE @teacherID int;
set @teacherID = (SELECT TeacherID FROM Teacher WHERE TeacherName=@TeacherName)

INSERT INTO Game(GameName,[Level],CategoryID,TeacherID,NumberOfQuestions,Confirmed)
VALUES (@gameName,@level,@catID,@teacherID,@numOfQuestions,0)
SET @output=@@ROWCOUNT
END

GO
/****** Object:  StoredProcedure [dbo].[saveQuestion]    Script Date: 12/21/2016 8:23:58 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE proc [dbo].[saveQuestion](
@statement nvarchar(max),
@type nvarchar(max),
@choices nvarchar(max),
@gameName nvarchar(max),
@answer int,
@output bit out
)
AS
BEGIN
SET NOCOUNT ON;
declare @ID int
SELECT @ID=GameID FROM Game WHERE GameName=@gameName
INSERT INTO Question(QuestionStatement,[Type],Choices,GameID,CorrectAnswer)
VALUES(@statement,@Type,@choices,@ID,@answer)
SET @output=@@ROWCOUNT

END

GO
/****** Object:  StoredProcedure [dbo].[saveScore]    Script Date: 12/21/2016 8:23:58 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE proc [dbo].[saveScore](
@name nvarchar(300),
@score int,
@gameName nvarchar(300),
@output bit out
)
AS
BEGIN
SET NOCOUNT ON;
declare @currentScore int;
SELECT @currentScore=Score FROM Student WHERE StudentName=@name COLLATE Latin1_General_CS_AS 


declare @exist int , @stID int , @gameID int, @gameScore int;
SET @gameScore = 0;
SET @output=1;
SELECT @stID=StudentID FROM Student WHERE StudentName=@name COLLATE Latin1_General_CS_AS ;
SET @output=@output&@@ROWCOUNT;
SELECT @gameID=GameID FROM Game Where GameName = @gameName COLLATE Latin1_General_CS_AS ;
SET @output=@output&@@ROWCOUNT;
SELECT @gameScore=Score FROM StudentGameScore WHERE StudentID=@stID AND GameID=@gameID;
SET @output=@output&@@ROWCOUNT;
SELECT @exist=COUNT(StudentID) FROM StudentGameScore WHERE StudentID=@stID AND GameID=@gameID;
SET @output=@output&@@ROWCOUNT;

declare @def int;
SET @def = @score-@gameScore;
	
IF @score > @gameScore
	SET @gameScore=@score;
IF @exist = 1
	UPDATE StudentGameScore SET Score =@gameScore WHERE StudentID=@stID AND GameID=@gameID;
ELSE
	INSERT INTO StudentGameScore(StudentID,GameID,Score) VALUES(@stID,@gameID,@score);

SET @output=@output&@@ROWCOUNT;

IF @def > 0
	SET @currentScore = @currentScore + @def;

UPDATE Student SET Score = @currentScore WHERE StudentName=@name COLLATE Latin1_General_CS_AS 
SET @output=@output&@@ROWCOUNT;
declare @badge nvarchar(MAX);
IF @currentScore >= 0 AND @currentScore <= 50
	SET @badge='Beginner';
ELSE IF @currentScore >= 51 AND @currentScore <= 150
	SET @badge='Mature';
ELSE IF @currentScore >= 151 AND @currentScore <= 300
	SET @badge='Specialist';
ELSE
	SET @badge='Expert' ;
UPDATE Student SET Badge=@badge WHERE StudentName=@name COLLATE Latin1_General_CS_AS ;

SET @output=@output&@@ROWCOUNT;
END




GO
/****** Object:  StoredProcedure [dbo].[saveStudent]    Script Date: 12/21/2016 8:23:58 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE proc [dbo].[saveStudent](
@name nvarchar(300),
@country nvarchar(MAX),
@birthdate date,
@gender nvarchar(10),
@email nvarchar(MAX),
@password nvarchar(MAX),
@output bit out
)
AS
BEGIN
SET NOCOUNT ON;
INSERT INTO Student(StudentName,Country,Birthdate,Gender,[E-mail],Password,Score,Badge)
VALUES(@name,@country,@birthdate,@gender,@email,@password,0,'Beginner')
SET @output=@@ROWCOUNT
END

GO
/****** Object:  StoredProcedure [dbo].[saveTeacher]    Script Date: 12/21/2016 8:23:58 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE proc [dbo].[saveTeacher](
@name nvarchar(300),
@country nvarchar(MAX),
@birthdate date,
@gender nvarchar(10),
@email nvarchar(MAX),
@password nvarchar(MAX),
@output bit out
)
AS
BEGIN
SET NOCOUNT ON;
INSERT INTO Teacher(TeacherName,Country,Birthdate,Gender,[E-mail],Password,Verified)
VALUES(@name,@country,@birthdate,@gender,@email,@password,0)
SET @output=@@ROWCOUNT
END

GO
/****** Object:  StoredProcedure [dbo].[setGameConfirmed]    Script Date: 12/21/2016 8:23:58 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE proc [dbo].[setGameConfirmed] (@gameName AS nvarchar(300),@output int OUT) AS
BEGIN
SET NOCOUNT ON;
Update Game SET Confirmed = 1 WHERE GameName=@gameName COLLATE Latin1_General_CS_AS ;
SET @output=@@ROWCOUNT

END

GO
/****** Object:  StoredProcedure [dbo].[setVerified]    Script Date: 12/21/2016 8:23:58 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE proc [dbo].[setVerified](
@name nvarchar(300),
@output bit out
)
AS
BEGIN
SET NOCOUNT ON;
UPDATE Teacher SET Verified=1 Where TeacherName=@name COLLATE Latin1_General_CS_AS 
SET @output=@@ROWCOUNT
END

GO
/****** Object:  StoredProcedure [dbo].[studentExists]    Script Date: 12/21/2016 8:23:58 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE proc [dbo].[studentExists](@name nvarchar(300),@email nvarchar(MAX),@output bit out)
AS
BEGIN
SET NOCOUNT ON;
SELECT @output=COUNT(@name) FROM Student WHERE StudentName=@name COLLATE Latin1_General_CS_AS 
IF(@output=0)
SELECT @output=COUNT(@email) FROM Student WHERE [E-mail]=@email COLLATE Latin1_General_CS_AS 
END

GO
/****** Object:  StoredProcedure [dbo].[teacherExists]    Script Date: 12/21/2016 8:23:58 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE proc [dbo].[teacherExists](@name nvarchar(300),@email nvarchar(MAX),@output bit out)
AS
BEGIN
SET NOCOUNT ON;
SELECT @output=COUNT(@name) FROM Teacher WHERE TeacherName=@name COLLATE Latin1_General_CS_AS 
IF(@output=0)
SELECT @output=COUNT(@email) FROM Teacher WHERE [E-mail]=@email COLLATE Latin1_General_CS_AS 
END

GO
/****** Object:  Table [dbo].[Admin]    Script Date: 12/21/2016 8:23:58 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Admin](
	[AdminID] [int] IDENTITY(1,1) NOT NULL,
	[AdminName] [nvarchar](300) NOT NULL,
	[Country] [nvarchar](max) NULL,
	[Birthdate] [date] NULL,
	[Gender] [nvarchar](10) NULL,
	[E-mail] [nvarchar](max) NULL,
	[Password] [nvarchar](max) NOT NULL
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO
/****** Object:  Table [dbo].[Blacklist]    Script Date: 12/21/2016 8:23:58 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Blacklist](
	[Email] [nvarchar](300) NOT NULL,
 CONSTRAINT [PK_Blacklist] PRIMARY KEY CLUSTERED 
(
	[Email] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[Category]    Script Date: 12/21/2016 8:23:58 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Category](
	[CategoryID] [int] IDENTITY(1,1) NOT NULL,
	[CategoryName] [nvarchar](300) NOT NULL,
 CONSTRAINT [PK_Category] PRIMARY KEY CLUSTERED 
(
	[CategoryID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [IX_Category] UNIQUE NONCLUSTERED 
(
	[CategoryName] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[Game]    Script Date: 12/21/2016 8:23:58 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Game](
	[GameID] [int] IDENTITY(1,1) NOT NULL,
	[GameName] [nvarchar](300) NOT NULL,
	[Level] [int] NOT NULL,
	[CategoryID] [int] NOT NULL,
	[TeacherID] [int] NOT NULL,
	[NumberOfQuestions] [int] NOT NULL,
	[Confirmed] [bit] NOT NULL,
 CONSTRAINT [PK_Game_1] PRIMARY KEY CLUSTERED 
(
	[GameID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [IX_Game] UNIQUE NONCLUSTERED 
(
	[GameName] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[Question]    Script Date: 12/21/2016 8:23:58 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Question](
	[QuestionID] [int] IDENTITY(1,1) NOT NULL,
	[QuestionStatement] [nvarchar](max) NOT NULL,
	[Type] [nvarchar](max) NULL,
	[Choices] [nvarchar](max) NOT NULL,
	[GameID] [int] NOT NULL,
	[CorrectAnswer] [int] NOT NULL,
 CONSTRAINT [PK_Question] PRIMARY KEY CLUSTERED 
(
	[QuestionID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO
/****** Object:  Table [dbo].[Student]    Script Date: 12/21/2016 8:23:58 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Student](
	[StudentID] [int] IDENTITY(1,1) NOT NULL,
	[StudentName] [nvarchar](300) NOT NULL,
	[Country] [nvarchar](max) NULL,
	[Birthdate] [date] NULL,
	[Gender] [nvarchar](10) NULL,
	[E-mail] [nvarchar](max) NOT NULL,
	[Password] [nvarchar](max) NOT NULL,
	[Score] [int] NULL,
	[Badge] [nvarchar](max) NULL,
 CONSTRAINT [PK_Student] PRIMARY KEY CLUSTERED 
(
	[StudentID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [IX_Student] UNIQUE NONCLUSTERED 
(
	[StudentName] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO
/****** Object:  Table [dbo].[StudentGameScore]    Script Date: 12/21/2016 8:23:58 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[StudentGameScore](
	[StudentID] [int] NOT NULL,
	[GameID] [int] NOT NULL,
	[Score] [int] NULL,
 CONSTRAINT [PK_StudentGameScore] PRIMARY KEY CLUSTERED 
(
	[StudentID] ASC,
	[GameID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[Teacher]    Script Date: 12/21/2016 8:23:58 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Teacher](
	[TeacherID] [int] IDENTITY(1,1) NOT NULL,
	[TeacherName] [nvarchar](300) NOT NULL,
	[Country] [nvarchar](max) NULL,
	[Birthdate] [date] NULL,
	[Gender] [nvarchar](10) NULL,
	[E-mail] [nvarchar](max) NOT NULL,
	[Password] [nvarchar](max) NOT NULL,
	[Verified] [bit] NOT NULL,
 CONSTRAINT [PK_Teacher] PRIMARY KEY CLUSTERED 
(
	[TeacherID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [IX_Teacher] UNIQUE NONCLUSTERED 
(
	[TeacherName] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO
ALTER TABLE [dbo].[Game]  WITH CHECK ADD  CONSTRAINT [FK_Game_Category] FOREIGN KEY([CategoryID])
REFERENCES [dbo].[Category] ([CategoryID])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[Game] CHECK CONSTRAINT [FK_Game_Category]
GO
ALTER TABLE [dbo].[Game]  WITH CHECK ADD  CONSTRAINT [FK_Game_Teacher] FOREIGN KEY([TeacherID])
REFERENCES [dbo].[Teacher] ([TeacherID])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[Game] CHECK CONSTRAINT [FK_Game_Teacher]
GO
ALTER TABLE [dbo].[Question]  WITH CHECK ADD  CONSTRAINT [FK_Question_Game] FOREIGN KEY([GameID])
REFERENCES [dbo].[Game] ([GameID])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[Question] CHECK CONSTRAINT [FK_Question_Game]
GO
ALTER TABLE [dbo].[StudentGameScore]  WITH CHECK ADD  CONSTRAINT [FK_StudentGameScore_Game] FOREIGN KEY([GameID])
REFERENCES [dbo].[Game] ([GameID])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[StudentGameScore] CHECK CONSTRAINT [FK_StudentGameScore_Game]
GO
ALTER TABLE [dbo].[StudentGameScore]  WITH CHECK ADD  CONSTRAINT [FK_StudentGameScore_Student] FOREIGN KEY([StudentID])
REFERENCES [dbo].[Student] ([StudentID])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[StudentGameScore] CHECK CONSTRAINT [FK_StudentGameScore_Student]
GO
USE [master]
GO
ALTER DATABASE [ST-Comm] SET  READ_WRITE 
GO
use [ST-Comm]
INSERT INTO [Category](CategoryName) VALUES('English')
INSERT INTO [Category](CategoryName) VALUES('Math')
INSERT INTO [Category](CategoryName) VALUES('Technology')
INSERT INTO [Category](CategoryName) VALUES('Science')
INSERT INTO [Admin](AdminName,Country,Birthdate,[E-mail],Gender,[Password]) VALUES('AndrewEmad','Egypt','1995-9-2','andrewen2010@yahoo.com','Male','12345678')
INSERT INTO [Teacher](TeacherName,Country,Birthdate,Gender,[E-mail],[Password],Verified) VALUES('AhmedHussein','Egypt','1995-9-2','Male','ahkcsit@gmail.com','12345678',1)
INSERT INTO [Student](StudentName,Country,Birthdate,Gender,[E-mail],[Password],Score,Badge) VALUES('AhmedMohamed','Egypt','1995-9-2','Male','aabdelmeged70@yahoo.com','12345678',0,'Mature')
INSERT INTO [Student](StudentName,Country,Birthdate,Gender,[E-mail],[Password],Score,Badge) VALUES('MariamAshraf','Egypt','1995-9-2','Female','mariamashraf096@gmail.com','12345678',0,'Beginner')
INSERT INTO [Game](GameName,[Level],CategoryID,TeacherID,NumberOfQuestions,Confirmed) VALUES('Opposites',1,1,1,2,1)
INSERT INTO [Game](GameName,[Level],CategoryID,TeacherID,NumberOfQuestions,Confirmed) VALUES('NaughtyPowers!!',2,2,1,2,1)
INSERT INTO [Game](GameName,[Level],CategoryID,TeacherID,NumberOfQuestions,Confirmed) VALUES('DataTypes',1,3,1,2,1)
INSERT INTO [Game](GameName,[Level],CategoryID,TeacherID,NumberOfQuestions,Confirmed) VALUES('BodyParts',2,4,1,2,1)
INSERT INTO [Question](QuestionStatement,[Type],Choices,GameID,CorrectAnswer) VALUES('What is the opposite of dangerous','MCQ','wave;save;safe;cave',1,2)
INSERT INTO [Question](QuestionStatement,[Type],Choices,GameID,CorrectAnswer) VALUES('What is the opposite of right','MCQ','left;sheft;theft;gift',1,0)
INSERT INTO [Question](QuestionStatement,[Type],Choices,GameID,CorrectAnswer) VALUES('3^2 = ?','MCQ','-1;0;1000;9',2,3)
INSERT INTO [Question](QuestionStatement,[Type],Choices,GameID,CorrectAnswer) VALUES('5^0 = ?','MCQ','infinity;undefied;1;0',2,2)
INSERT INTO [Question](QuestionStatement,[Type],Choices,GameID,CorrectAnswer) VALUES('There is no difference between char and String','TRUEorFALSE','True;False',3,1)
INSERT INTO [Question](QuestionStatement,[Type],Choices,GameID,CorrectAnswer) VALUES('double variables can represent numbers having floating point','TRUEorFALSE','True;False',3,0)
INSERT INTO [Question](QuestionStatement,[Type],Choices,GameID,CorrectAnswer) VALUES('A human has four eyes and one leg','TRUEorFALSE','True;False',4,1)
INSERT INTO [Question](QuestionStatement,[Type],Choices,GameID,CorrectAnswer) VALUES('It is important to clean your teeth every day','TRUEorFALSE','True;False',4,0)